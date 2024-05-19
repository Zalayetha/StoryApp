package com.zaghy.storyapp.home.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import com.zaghy.storyapp.DataDummy
import com.zaghy.storyapp.MainDispatcherRule
import com.zaghy.storyapp.home.model.ListStoryItem
import com.zaghy.storyapp.network.repository.StoryRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest{
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
//
//
//    @Test
//    fun 'when Get Stories Should Not Null and Return Data'() = runTest {
//        val dummyStories = DataDummy.generateDummyStoriesResponse()
//        val data:PagingData<ListStoryItem> = ListStorySource.snapshot()
//    }
}