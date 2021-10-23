package com.khair.socialposts_model.remote

import com.khair.socialposts_model.entities.Comment
import com.khair.socialposts_model.entities.Post
import com.khair.socialposts_model.entities.User
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PostsApiRest {

    @GET("posts/")
    suspend fun getAllPosts(@Query("userId") userId: Int): List<Post>?

    @GET("users/{userId}")
    suspend fun getUser(@Path("userId") userId: Int ): User?

    @GET("comments/")
    suspend fun getPostComments(@Query("postId") postId: Int): List<Comment>?
}