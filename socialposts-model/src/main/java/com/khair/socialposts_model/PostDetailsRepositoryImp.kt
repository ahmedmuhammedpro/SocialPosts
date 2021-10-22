package com.khair.socialposts_model

import com.khair.socialposts_model.entities.Comment
import com.khair.socialposts_model.entities.User
import com.khair.socialposts_model.remote.PostsApi

class PostDetailsRepositoryImp : PostDetailsRepository {

    private val postsApi by lazy {
        PostsApi.getPostsApi()
    }

    override suspend fun getCommentsByPostId(postId: Int): Result<List<Comment>?> {
        return try {
            val comments = postsApi.getPostComments(postId)
            if (!comments.isNullOrEmpty()) {
                Result.success(comments)
            } else {
                Result.failure(Exception("Null or Empty posts"))
            }
        } catch (ex: Throwable) {
            Result.failure(ex)
        }
    }

    override suspend fun userById(userId: Int): Result<User?> {
        return try {
            val user = postsApi.getUser(userId)
            if (user != null) {
                Result.success(user)
            } else {
                Result.failure(Exception("Null or Empty posts"))
            }
        } catch (ex: Throwable) {
            Result.failure(ex)
        }
    }
}