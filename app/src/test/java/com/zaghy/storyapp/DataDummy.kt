package com.zaghy.storyapp

import com.zaghy.storyapp.home.model.ListStoryItem

object DataDummy {

    fun generateDummyStoriesResponse():List<ListStoryItem>{
        val items:MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100){
            val story = ListStoryItem(
                id = i.toString(),
                name = "name $i",
                description = "description $i",
            )
            items.add(story)
        }
        return items

    }
}