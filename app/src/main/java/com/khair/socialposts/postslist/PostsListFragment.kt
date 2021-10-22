package com.khair.socialposts.postslist

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.khair.socialposts.R
import com.khair.socialposts.databinding.FragmentPostsListBinding
import com.khair.socialposts_model.PostsRepository
import com.khair.socialposts_model.PostsRepositoryImp
import com.khair.socialposts_model.entities.Post
import com.khair.socialposts_model.local.PostsDatabase

class PostsListFragment : Fragment(), PostsAdapter.OnItemClickListener {

    private lateinit var binding: FragmentPostsListBinding
    private lateinit var postsViewModel: PostsViewModel
    private var postsAdapter = PostsAdapter(arrayListOf(), this)
    private val postsRepository: PostsRepository by lazy {
        PostsRepositoryImp(PostsDatabase.getDatabase(requireContext()).postsDao)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_posts_list, container, false)
        postsViewModel = PostsViewModel(postsRepository)

        binding.postsRv.layoutManager = LinearLayoutManager(context)
        binding.postsRv.adapter = postsAdapter
        postsViewModel.getPosts()
        setupRequestsListener()
        setupViewsListener()

        return binding.root
    }

    private fun setupViewsListener() {
        binding.tryAgain.setOnClickListener {
            binding.errorViewContainer.visibility = View.GONE
            binding.loadingViewContainer.visibility = View.VISIBLE
            postsViewModel.getPosts()
        }

        binding.refresh.setOnRefreshListener {
            postsViewModel.getPosts()
            binding.refresh.isRefreshing = false
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setupRequestsListener() {

        postsViewModel.postsLiveDate.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                binding.postsContentContainer.visibility = View.VISIBLE
                postsAdapter.posts.clear()
                postsAdapter.posts.addAll(it)
                postsAdapter.notifyDataSetChanged()
            } else {
                binding.postsContentContainer.visibility = View.GONE
                binding.errorViewContainer.visibility = View.VISIBLE
            }
        }

        postsViewModel.loadingLiveData.observe(viewLifecycleOwner) {
            if (it) {
                binding.loadingViewContainer.visibility = View.VISIBLE
                binding.postsContentContainer.visibility = View.GONE
                binding.errorViewContainer.visibility = View.GONE
            } else {
                binding.loadingViewContainer.visibility = View.GONE
            }
        }

        postsViewModel.failingLiveData.observe(viewLifecycleOwner) {
            showSnackBar(getString(R.string.general_error))
        }

    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

    override fun onItemClick(view: View, item: Post) {

    }
}