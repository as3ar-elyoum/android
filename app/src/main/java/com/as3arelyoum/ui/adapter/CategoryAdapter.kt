package com.as3arelyoum.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.as3arelyoum.R
import com.as3arelyoum.data.model.Category
import com.as3arelyoum.databinding.CategoryCardBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class CategoryAdapter(
    private val onItemClicked: (position: Int) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CustomViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val recyclerCard =
            CategoryCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomViewHolder(recyclerCard, onItemClicked)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val categoryItems = differ.currentList[position]
        holder.binding.apply {
            Glide.with(holder.binding.root.context)
                .load(categoryItems.icon)
                .placeholder(R.drawable.ic_downloading)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(categoryImage)
            categoryNameTv.text = categoryItems.name
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class CustomViewHolder(
        val binding: CategoryCardBinding,
        private val onItemClicked: (position: Int) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            binding.categoryCard.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            onItemClicked(absoluteAdapterPosition)
        }
    }
}
