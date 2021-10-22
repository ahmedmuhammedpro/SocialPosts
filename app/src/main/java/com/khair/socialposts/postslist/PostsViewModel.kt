package com.khair.socialposts.postslist

import androidx.lifecycle.*
import com.khair.socialposts_model.PostsRepository
import com.khair.socialposts_model.entities.Post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class PostsViewModel(private val postsRepository: PostsRepository) : ViewModel() {

    private val mCachedPostsLiveDate = MutableLiveData<List<Post>?>()
    val cachedPostsLiveDate: LiveData<List<Post>?> = mCachedPostsLiveDate

    private val mPostsLiveDate = MutableLiveData<List<Post>?>()
    val postsLiveDate: LiveData<List<Post>?> = mPostsLiveDate

    private val mLoadingLiveData = MutableLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean> = mLoadingLiveData

    private val mFailingLiveData = MutableLiveData<Boolean>()
    val failingLiveData: LiveData<Boolean> = mFailingLiveData

    fun getPosts() {
        mLoadingLiveData.value = true
        viewModelScope.launch(Dispatchers.IO) {
            var posts: List<Post>? = null

            try {
                // Get remote posts
                val postsResult = postsRepository.getAllPostsRemote()
                posts = postsResult.getOrThrow()
            } catch (ex: Throwable) {
                Timber.e(ex)
                withContext(Dispatchers.Main) {
                    mFailingLiveData.value = false
                }
            }

            withContext(Dispatchers.Main) {
                mPostsLiveDate.value = posts
                mLoadingLiveData.value = false
            }

            if (!posts.isNullOrEmpty()) {
                postsRepository.deleteAllPostsLocal()
                postsRepository.insertPostsLocal(posts)
            }
        }
    }

    fun getCachedPosts() {
        viewModelScope.launch(Dispatchers.IO) {
        }
    }

}

@Suppress("UNCHECKED_CAST")
class AirlineListingViewModelFactory(private val postsRepository: PostsRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        (PostsViewModel(postsRepository) as T)

}