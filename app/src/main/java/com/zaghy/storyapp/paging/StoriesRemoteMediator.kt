package com.zaghy.storyapp.paging

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.zaghy.storyapp.home.model.ListStoryItem
import com.zaghy.storyapp.local.room.RemoteKeys
import com.zaghy.storyapp.local.room.StoriesDatabase
import com.zaghy.storyapp.network.retrofit.ApiService

@OptIn(ExperimentalPagingApi::class)
class StoriesRemoteMediator(
    private val apiService: ApiService,
    private val storiesDatabase: StoriesDatabase,
    private val token: String
) : RemoteMediator<Int, ListStoryItem>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
        const val TAG = "StoriesRemoteMediator"
    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }


    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ListStoryItem>
    ): MediatorResult {

        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            }

            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }
        try {
            val responseData = apiService.getStories(
                token = "Bearer $token",
                page = page,
                size = state.config.pageSize
            )
            val endOfPaginationReached = responseData.listStory?.isEmpty()
            Log.d(TAG, endOfPaginationReached.toString())
            storiesDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    Log.d(TAG, "refresh")
                    storiesDatabase.remoteKeysDao().deleteRemoteKeys()
                    storiesDatabase.storiesDao().deleteAll()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached!!) null else page + 1
                val key = responseData.listStory.map {
                    it?.let { it1 -> RemoteKeys(id = it1.id, prevKey = prevKey, nextKey = nextKey) }
                }
                val keys = key
                val nonNullKeys = keys.filterNotNull()
                storiesDatabase.remoteKeysDao().insertAll(nonNullKeys)


                val stories = responseData.listStory
                val nonNullStories = stories.filterNotNull()
                storiesDatabase.storiesDao().insertStories(nonNullStories)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached ?: true)
        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, ListStoryItem>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            storiesDatabase.remoteKeysDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, ListStoryItem>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            storiesDatabase.remoteKeysDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, ListStoryItem>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                storiesDatabase.remoteKeysDao().getRemoteKeysId(id)
            }
        }


    }
}