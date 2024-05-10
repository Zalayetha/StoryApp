package com.zaghy.storyapp.addstory.model

import com.google.gson.annotations.SerializedName

data class MResponseAddStory(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)
