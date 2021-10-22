package com.khair.socialposts_model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Post(@PrimaryKey @SerializedName("id") val postId: Int, val userId: Int, val title: String, val body: String)
