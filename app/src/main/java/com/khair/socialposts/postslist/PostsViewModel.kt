package com.khair.socialposts.postslist

import androidx.lifecycle.*
import com.khair.socialposts_model.PostsRepository
import com.khair.socialposts_model.entities.Post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class PostsViewModel(private val postsRepository: PostsRepository) : ViewModel() {

    private val mCachedPostsLiveDate = MutableLiveData<List<Post>>()
    val cachedPostsLiveDate: LiveData<List<Post>> = mCachedPostsLiveDate

    private val mFirstPostsLiveDate = MutableLiveData<List<Post>?>()
    val firstPostsLiveDate: LiveData<List<Post>?> = mFirstPostsLiveDate

    private val mMorePostsLiveDate = MutableLiveData<List<Post>?>()
    val morePostsLiveDate: LiveData<List<Post>?> = mMorePostsLiveDate

    private val mLoadingLiveData = MutableLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean> = mLoadingLiveData

    private val mFailingLiveData = MutableLiveData<Boolean>()
    val failingLiveData: LiveData<Boolean> = mFailingLiveData


    fun getFirstPosts() {
        mLoadingLiveData.value = true
        viewModelScope.launch(Dispatchers.IO) {
            var posts: List<Post>? = null

            try {
                // Get remote posts
                val postsResult = postsRepository.getAllPostsRemote(1)
                posts = postsResult.getOrThrow()
            } catch (ex: Throwable) {
                Timber.e(ex)
                withContext(Dispatchers.Main) {
                    mFailingLiveData.value = false
                }
            }

            withContext(Dispatchers.Main) {
                mFirstPostsLiveDate.value = posts
                mLoadingLiveData.value = false
            }

            if (!posts.isNullOrEmpty()) {
                val endIndex = if (posts.size > 10) { 10 } else { posts.size }
                postsRepository.deleteAllPostsLocal()
                postsRepository.insertPostsLocal(posts.subList(0, endIndex))
            }
        }
    }

    fun getMorePosts(userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            var posts: List<Post>? = null

            try {
                // Get remote posts
                val postsResult = postsRepository.getAllPostsRemote(userId)
                posts = postsResult.getOrThrow()
            } catch (ex: Throwable) {
                Timber.e(ex)
                withContext(Dispatchers.Main) {
                    mFailingLiveData.value = false
                }
            }

            withContext(Dispatchers.Main) {
                mMorePostsLiveDate.value = posts
                mLoadingLiveData.value = false
            }
        }
    }

    fun getCachedPosts() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val posts = postsRepository.getAllPostsLocal()
                withContext(Dispatchers.Main) {
                    mCachedPostsLiveDate.value = posts
                }
            } catch (ex: Throwable) {
                Timber.e(ex)
            }
        }
    }

}

@Suppress("UNCHECKED_CAST")
class PostsViewModelFactory(private val postsRepository: PostsRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        (PostsViewModel(postsRepository) as T)

}