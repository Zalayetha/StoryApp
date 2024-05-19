package com.zaghy.storyapp.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.zaghy.storyapp.home.model.ListStoryItem
import com.zaghy.storyapp.network.retrofit.ApiService

class StoriesPagingSource(private val apiService: ApiService,private val token:String): PagingSource<Int, ListStoryItem>() {
    companion object{
        const val INITIAL_PAGE_INDEX = 1
        private const val TAG = "StoriesPagingSource"
    }
    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)

        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getStories(token=token, page = position, size = params.loadSize)
            val stories = responseData.listStory ?: emptyList()
            val nonNullStories = stories.filterNotNull()
            LoadResult.Page(
                data = nonNullStories ,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (responseData.listStory.isNullOrEmpty()) null else position + 1
            )
        }catch (exception : Exception){
            return LoadResult.Error(exception)
        }
    }
}