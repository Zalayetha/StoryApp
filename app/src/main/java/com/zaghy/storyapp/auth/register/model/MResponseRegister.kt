package com.zaghy.storyapp.auth.register.model

import com.google.gson.annotations.SerializedName

data class MResponseRegister(

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)
