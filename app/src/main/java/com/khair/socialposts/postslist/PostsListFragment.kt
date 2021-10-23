package com.khair.socialposts.postslist

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.khair.socialposts.R
import com.khair.socialposts.databinding.FragmentPostsListBinding
import com.khair.socialposts.postdetails.PostDetailsFragment
import com.khair.socialposts_model.PostsRepository
import com.khair.socialposts_model.PostsRepositoryImp
import com.khair.socialposts_model.entities.Post
import com.khair.socialposts_model.local.PostsDatabase

class PostsListFragment : Fragment(), PostsAdapter.OnItemClickListener {

    private lateinit var binding: FragmentPostsListBinding
    private lateinit var postsViewModel: PostsViewModel
    private var mRootView: View? = null
    private var cachedPosts: List<Post>? = null
    private var postsAdapter = PostsAdapter(arrayListOf(), this)
    private val postsRepository: PostsRepository by lazy {
        PostsRepositoryImp(PostsDatabase.getDatabase(requireContext()).postsDao)
    }

    private var hasMorePosts = true
    private var nextUserPosts = 2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (mRootView == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_posts_list, container, false)
            postsViewModel = ViewModelProvider(
                requireActivity(),
                PostsViewModelFactory(postsRepository)
            ).get(PostsViewModel::class.java)

            binding.postsRv.layoutManager = LinearLayoutManager(context)
            binding.postsRv.adapter = postsAdapter
            postsViewModel.getCachedPosts()
            setupRequestsListener()
            setupViewsListener()

            mRootView = binding.root
        }

        return mRootView
    }

    private fun setupViewsListener() {
        
        binding.postsRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastVisibleItem = (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()

                if (hasMorePosts && lastVisibleItem == recyclerView.adapter!!.itemCount - 1)  {
                    binding.loadMore.visibility = View.VISIBLE
                    postsViewModel.getMorePosts(nextUserPosts)
                    hasMorePosts = false
                }
            }
        })

        binding.tryAgain.setOnClickListener {
            binding.errorViewContainer.visibility = View.GONE
            binding.loadingViewContainer.visibility = View.VISIBLE
            postsViewModel.getFirstPosts()
        }

        binding.refresh.setOnRefreshListener {
            binding.refresh.isRefreshing = false
            postsViewModel.getFirstPosts()
            hasMorePosts = true
            nextUserPosts = 2
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setupRequestsListener() {

        postsViewModel.cachedPostsLiveDate.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                cachedPosts = it
                binding.postsContentContainer.visibility = View.VISIBLE
                postsAdapter.posts.clear()
                postsAdapter.posts.addAll(it)
                postsAdapter.notifyDataSetChanged()
            }

            postsViewModel.getFirstPosts()
        }

        postsViewModel.firstPostsLiveDate.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                binding.postsContentContainer.visibility = View.VISIBLE
                postsAdapter.posts.clear()
                postsAdapter.posts.addAll(it)
                postsAdapter.notifyDataSetChanged()
            } else {
                if (cachedPosts.isNullOrEmpty()) {
                    binding.postsContentContainer.visibility = View.GONE
                    binding.errorViewContainer.visibility = View.VISIBLE
                    hasMorePosts = true
                }
            }
        }

        postsViewModel.morePostsLiveDate.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                postsAdapter.posts.addAll(it)
                postsAdapter.notifyItemRangeChanged(postsAdapter.itemCount, it.size + 1)
                hasMorePosts = true
                nextUserPosts++
            }

            binding.loadMore.visibility = View.GONE
        }

        postsViewModel.loadingLiveData.observe(viewLifecycleOwner) {
            if (it) {
                if (cachedPosts.isNullOrEmpty()) {
                    binding.loadingViewContainer.visibility = View.VISIBLE
                    binding.postsContentContainer.visibility = View.GONE
                    binding.errorViewContainer.visibility = View.GONE
                } else {
                    binding.loadingViewContainer.visibility = View.VISIBLE
                    binding.errorViewContainer.visibility = View.GONE
                }
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
        val postDetailsBundle = Bundle().apply {
            putParcelable(PostDetailsFragment.POST_ITEM_KEY, item)
        }
        findNavController().navigate(R.id.postDetailsFragment, postDetailsBundle)
    }
}