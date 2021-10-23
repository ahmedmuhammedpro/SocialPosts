package com.khair.socialposts.postslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.khair.socialposts.R
import com.khair.socialposts_model.entities.Post

class PostsAdapter(
    val posts: ArrayList<Post>, private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<PostsAdapter.PostViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.post_item, parent, false)

        val holder = PostViewHolder(view)
        view.setOnClickListener {
            onItemClickListener.onItemClick(view, posts[holder.adapterPosition])
        }
        holder.viewMore.setOnClickListener {
            onItemClickListener.onItemClick(view, posts[holder.adapterPosition])
        }

        return holder
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.postTitleTV.text = posts[position].title
        holder.postBodyTv.text = posts[position].body
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    class PostViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val postTitleTV: TextView = view.findViewById(R.id.post_title_tv)
        val postBodyTv: TextView = view.findViewById(R.id.post_body_tv)
        val viewMore: ImageView = view.findViewById(R.id.post_view_more)
    }

    interface OnItemClickListener {
        fun onItemClick(view: View, item: Post)
    }

}