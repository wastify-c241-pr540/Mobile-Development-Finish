package com.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.data.model.PostModel
import com.ramm.wastify.R

class PostAdapter(private val onClick: (PostModel) -> Unit) :
    ListAdapter<PostModel, PostAdapter.ProductViewHolder>(ProductCallBack) {

    class ProductViewHolder(itemView: View, val onClick: (PostModel) -> Unit) :
        RecyclerView.ViewHolder(itemView) {

        private val image: ImageView = itemView.findViewById(R.id.img_item_photo)
        private val title: TextView = itemView.findViewById(R.id.tv_item_name)

        private var currentPost: PostModel? = null

        init {
            itemView.setOnClickListener {
                currentPost?.let {
                    onClick(it)
                }
            }
        }

        fun bind(post: PostModel) {
            currentPost = post
            title.text = post.title
            Glide.with(itemView).load(post.thumbnail).centerCrop().into(image)
        }
}

object ProductCallBack : DiffUtil.ItemCallback<PostModel>() {
    override fun areItemsTheSame(oldItem: PostModel, newItem: PostModel): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: PostModel, newItem: PostModel): Boolean {
        return oldItem.id == newItem.id
    }
}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_home, parent, false)
        return ProductViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}