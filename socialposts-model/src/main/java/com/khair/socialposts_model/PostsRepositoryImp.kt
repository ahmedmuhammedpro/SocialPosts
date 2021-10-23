package com.khair.socialposts_model

import com.khair.socialposts_model.entities.Post
import com.khair.socialposts_model.local.PostsDao
import com.khair.socialposts_model.remote.PostsApi

class PostsRepositoryImp(private val postsDao: PostsDao) : PostsRepository {

    private val postsApi by lazy {
        PostsApi.getPostsApi()
    }

    override suspend fun getAllPostsRemote(userId: Int): Result<List<Post>?> {
        return try {
            val posts = postsApi.getAllPosts(userId)
            Result.success(posts)
        } catch (ex: Throwable) {
            Result.failure(ex)
        }
    }

    override suspend fun getAllPostsLocal(): List<Post>? {
        return postsDao.getPosts()
    }

    override suspend fun insertPostsLocal(posts: List<Post>) {
        postsDao.insert(posts)
    }

    override suspend fun deleteAllPostsLocal() {
        postsDao.deleteAllPosts()
    }


}