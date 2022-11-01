package com.as3arelyoum.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.as3arelyoum.data.model.Category
import com.as3arelyoum.databinding.CategoryCardBinding
import com.bumptech.glide.Glide

class CategoryAdapter(
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
            Glide.with(holder.binding.root.context)
                .load(categoryItems.icon)
                .into(categoryImage)
            categoryNameTv.text = categoryItems.name
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
