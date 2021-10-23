package com.khair.socialposts_model

import com.khair.socialposts_model.entities.Post

interface PostsRepository {
    // remote
    suspend fun getAllPostsRemote(userId: Int): Result<List<Post>?>

    // local
    suspend fun getAllPostsLocal(): List<Post>?
    suspend fun insertPostsLocal(posts: List<Post>)
    suspend fun deleteAllPostsLocal()
}