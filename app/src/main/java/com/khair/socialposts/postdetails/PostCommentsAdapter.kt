package com.khair.socialposts.postdetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.khair.socialposts.R
import com.khair.socialposts_model.entities.Comment

class PostCommentsAdapter(private val comments: List<Comment>) :RecyclerView.Adapter<PostCommentsAdapter.CommentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.comment_item, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.commentAuthorTV.text = comments[position].username
        holder.commentTV.text = comments[position].body
    }

    override fun getItemCount(): Int {
        return comments.size
    }

    class CommentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val commentAuthorTV: TextView = view.findViewById(R.id.comment_author_name_tv)
        val commentTV: TextView = view.findViewById(R.id.comment_tv)
    }

}