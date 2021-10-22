package com.khair.socialposts_model.entities

import com.google.gson.annotations.SerializedName

data class Comment(
    val postId: Int, val id: Int, @SerializedName("name") val username: String,
    val email: String, val body: String
)
