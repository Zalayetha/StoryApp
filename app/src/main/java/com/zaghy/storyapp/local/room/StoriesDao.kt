package com.zaghy.storyapp.local.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zaghy.storyapp.home.model.ListStoryItem

@Dao
interface StoriesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStories(stories: List<ListStoryItem>)

    @Query("SELECT * FROM stories")
    fun getAllStories():PagingSource<Int,ListStoryItem>

    @Query("DELETE FROM stories")
    suspend fun deleteAll()

}