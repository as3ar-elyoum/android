package com.as3arelyoum.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.as3arelyoum.R
import com.as3arelyoum.data.models.Category
import com.as3arelyoum.databinding.CategoryCardBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class CategoriesAdapter(
    private val onItemClicked: (position: Int) -> Unit
) : RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>() {

    val categoryList = mutableListOf<Category>()

    @SuppressLint("NotifyDataSetChanged")
    fun setCategoriesList(categories: List<Category>) {
        categoryList.clear()
        categoryList.addAll(categories)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val recyclerCard =
            CategoryCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(recyclerCard, onItemClicked)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val categoryItems = categoryList[position]
        holder.binding.apply {
            Glide.with(root.context)
                .load(categoryItems.icon)
                .placeholder(R.drawable.ic_downloading)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(categoryImage)
            categoryNameTv.text = categoryItems.name
        }
    }

    override fun getItemCount() = categoryList.size

    inner class CategoryViewHolder(
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
