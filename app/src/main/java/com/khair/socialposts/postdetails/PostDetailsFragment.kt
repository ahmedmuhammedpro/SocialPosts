package com.khair.socialposts.postdetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.khair.socialposts.R
import com.khair.socialposts.databinding.FragmentPostDetailsBinding
import com.khair.socialposts_model.PostDetailsRepository
import com.khair.socialposts_model.PostDetailsRepositoryImp
import com.khair.socialposts_model.entities.Post

class PostDetailsFragment : Fragment() {

    private lateinit var binding: FragmentPostDetailsBinding
    private lateinit var postDetailsViewModel: PostDetailsViewModel
    private var post: Post? = null
    private val postDetailsRepository: PostDetailsRepository by lazy {
        PostDetailsRepositoryImp()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            post = it.getParcelable(POST_ITEM_KEY)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_post_details,
            container,
            false
        )
        postDetailsViewModel = ViewModelProvider(
            this, PostDetailsViewModelFactory(postDetailsRepository)
        ).get(PostDetailsViewModel::class.java)

        post?.let {
            postDetailsViewModel.getUserAndComments(it)
        }

        setupRequestsListener()

        return binding.root
    }

    private fun setupRequestsListener() {
        postDetailsViewModel.loadingLiveData.observe(viewLifecycleOwner) {
            binding.loadingViewContainer.visibility = if (it) View.VISIBLE else View.GONE
        }

        postDetailsViewModel.userAndCommentsLiveData.observe(viewLifecycleOwner) {
            val user = it.first
            val comments = it.second
            binding.post = post
            binding.userName = user.name
            binding.numOfComments = comments.size
            binding.commentsRv.layoutManager = LinearLayoutManager(requireContext())
            binding.commentsRv.adapter = PostCommentsAdapter(comments)

            binding.postDetailsContainer.visibility = View.VISIBLE
        }

        postDetailsViewModel.failingLiveData.observe(viewLifecycleOwner) {
            if (it) {
                Snackbar.make(binding.root, getString(R.string.general_error), Snackbar.LENGTH_LONG).show()
            }
        }
    }

    companion object {

        const val POST_ITEM_KEY = "POST_ITEM"

        fun newInstance(param1: String, param2: String) =
            PostDetailsFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}