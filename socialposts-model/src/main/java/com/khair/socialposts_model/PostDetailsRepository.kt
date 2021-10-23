package com.khair.socialposts_model

import com.khair.socialposts_model.entities.Comment
import com.khair.socialposts_model.entities.User

interface PostDetailsRepository {
    // remote
    suspend fun getCommentsByPostId(postId: Int): Result<List<Comment>>
    suspend fun getUserById(userId: Int): Result<User>
}