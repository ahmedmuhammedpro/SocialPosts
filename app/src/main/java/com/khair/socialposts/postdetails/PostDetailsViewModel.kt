package com.khair.socialposts.postdetails

import androidx.lifecycle.*
import com.khair.socialposts_model.PostDetailsRepository
import com.khair.socialposts_model.entities.Comment
import com.khair.socialposts_model.entities.Post
import com.khair.socialposts_model.entities.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class PostDetailsViewModel(private val postDetailsRepository: PostDetailsRepository) : ViewModel() {

    private val mUserAndCommentsLiveData = MutableLiveData<Pair<User,List<Comment>>>()
    val userAndCommentsLiveData: LiveData<Pair<User,List<Comment>>> = mUserAndCommentsLiveData

    private val mLoadingLiveData = MutableLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean> = mLoadingLiveData

    private val mFailingLiveData = MutableLiveData<Boolean>()
    val failingLiveData: LiveData<Boolean> = mFailingLiveData

    fun getUserAndComments(post: Post) {
        mLoadingLiveData.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val user = postDetailsRepository.getUserById(post.userId).getOrThrow()
                val comments = postDetailsRepository.getCommentsByPostId(post.postId).getOrThrow()
                val userAndComments = Pair(user, comments)
                withContext(Dispatchers.Main) {
                    mUserAndCommentsLiveData.value = userAndComments
                    mLoadingLiveData.value = false
                }
            } catch (ex: Throwable) {
                Timber.e(ex)
                withContext(Dispatchers.Main) {
                    mFailingLiveData.value = true
                    mLoadingLiveData.value = false
                }
            }
        }
    }

}

@Suppress("UNCHECKED_CAST")
class PostDetailsViewModelFactory(private val postsDetailsRepository: PostDetailsRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        (PostDetailsViewModel(postsDetailsRepository) as T)

}