package com.as3arelyoum.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.as3arelyoum.data.model.Category
import com.as3arelyoum.R
import com.as3arelyoum.databinding.CategoryCardBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class CategoryAdapter(
    var context: Context,
    private var list: List<Category>,
    private val onItemClicked: (position: Int) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val recyclerCard =
            CategoryCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomViewHolder(recyclerCard, onItemClicked)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val categoryItems = list[position]
        holder.binding.apply {
            Glide.with(context)
                .load(R.mipmap.ic_launcher_round)
                .placeholder(android.R.drawable.stat_sys_download)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(categoryIcon)

            categoryTitleTv.text = categoryItems.name
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class CustomViewHolder(
        val binding: CategoryCardBinding,
        private val onItemClicked: (position: Int) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            binding.root.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            onItemClicked(absoluteAdapterPosition)
        }
    }
}
