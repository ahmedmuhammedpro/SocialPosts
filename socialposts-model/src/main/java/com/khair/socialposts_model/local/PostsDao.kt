package com.khair.socialposts_model.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.khair.socialposts_model.entities.Post

@Dao
interface PostsDao {

    @Query("SELECT * FROM POST")
    suspend fun getPosts(): List<Post>?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(posts: List<Post>)

    @Query("DELETE FROM POST")
    suspend fun deleteAllPosts()

}