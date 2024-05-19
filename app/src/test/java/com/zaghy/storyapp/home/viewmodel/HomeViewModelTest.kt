package com.zaghy.storyapp.home.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import com.zaghy.storyapp.DataDummy
import com.zaghy.storyapp.MainDispatcherRule
import com.zaghy.storyapp.adapter.RecyclerViewStoryAdapter
import com.zaghy.storyapp.getOrAwaitValue
import com.zaghy.storyapp.home.model.ListStoryItem
import com.zaghy.storyapp.network.repository.StoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var storyRepository: StoryRepository

    val tokenTest =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLXprSlMtclBQanFPZnpLQUsiLCJpYXQiOjE3MTU0NDExODl9.YQguHqz11KgsRMoKR6j19wCQYcOJQxjhSHCo0D8n25w"

    val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }


    @Test
    fun `when Get Stories Should Not Null and Return Data`() = runTest {
        val dummyStories = DataDummy.generateDummyStoriesResponse()
        val data: PagingData<ListStoryItem> = StoryPagingSource.snapshot(dummyStories)
        val expectedStories = MutableLiveData<PagingData<ListStoryItem>>()
        expectedStories.value = data
        Mockito.`when`(storyRepository.listStories(tokenTest))
            .thenReturn(expectedStories)

        val homeViewModel = HomeViewModel(storyRepository)
        val actualStories: PagingData<ListStoryItem> =
            homeViewModel.getStories(tokenTest).getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = object : DiffUtil.ItemCallback<ListStoryItem>() {
                override fun areItemsTheSame(
                    oldItem: ListStoryItem,
                    newItem: ListStoryItem
                ): Boolean {
                    return oldItem == newItem
                }

                override fun areContentsTheSame(
                    oldItem: ListStoryItem,
                    newItem: ListStoryItem
                ): Boolean {
                    return oldItem == newItem
                }

            },
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )
        differ.submitData(actualStories)
        assertNotNull(differ.snapshot())
        assertEquals(dummyStories.size, differ.snapshot().size)
        assertEquals(dummyStories[0], differ.snapshot()[0])


    }

    @Test
    fun `when Get Stories Empty Should Return No Data`() = runTest {
        val data: PagingData<ListStoryItem> = PagingData.from(emptyList())
        val expectedStories = MutableLiveData<PagingData<ListStoryItem>>()
        expectedStories.value = data
        Mockito.`when`(storyRepository.listStories(tokenTest)).thenReturn(expectedStories)

        val homeViewModel = HomeViewModel(storyRepository)
        val actualStories: PagingData<ListStoryItem> =
            homeViewModel.getStories(tokenTest).getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = object : DiffUtil.ItemCallback<ListStoryItem>() {
                override fun areItemsTheSame(
                    oldItem: ListStoryItem,
                    newItem: ListStoryItem
                ): Boolean {
                    return oldItem == newItem
                }

                override fun areContentsTheSame(
                    oldItem: ListStoryItem,
                    newItem: ListStoryItem
                ): Boolean {
                    return oldItem == newItem
                }

            },
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main

        )
        differ.submitData(actualStories)
        assertEquals(0, differ.snapshot().size)

    }


    class StoryPagingSource : PagingSource<Int, LiveData<List<ListStoryItem>>>() {
        companion object {
            fun snapshot(items: List<ListStoryItem>): PagingData<ListStoryItem> {
                return PagingData.from(items)
            }
        }

        override fun getRefreshKey(state: PagingState<Int, LiveData<List<ListStoryItem>>>): Int {
            return 0
        }

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<ListStoryItem>>> {
            return LoadResult.Page(emptyList(), 0, 1)
        }
    }
}