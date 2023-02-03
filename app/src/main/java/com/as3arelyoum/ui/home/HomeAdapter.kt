package com.as3arelyoum.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.as3arelyoum.R
import com.as3arelyoum.data.remote.dto.CategoryDTO
import com.as3arelyoum.databinding.HomeCardBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class HomeAdapter(private val onItemClicked: (position: Int, position2: Int) -> Unit) :
    RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {
    var categoriesList: List<CategoryDTO> = ArrayList()

    @SuppressLint("NotifyDataSetChanged")
    fun setCategories(categories: List<CategoryDTO>) {
        categoriesList = categories
        notifyDataSetChanged()
    }

    override fun getItemCount() = categoriesList.size

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val categories = categoriesList[position]
        holder.bind(categories, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val recyclerCard =
            HomeCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(recyclerCard)
    }

    inner class HomeViewHolder(
        val binding: HomeCardBinding
    ) : ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(category: CategoryDTO, position: Int) {
            val productAdapter =
                ProductsAdapter { productsPosition -> onProductClicked(productsPosition) }
            productAdapter.setProducts(categoriesList[position].products)

//            val categoryId = products.first().category_id
//            val category = categoriesList.find { category -> category.id == categoryId }

            binding.apply {
                Glide.with(root.context)
                    .load(category.icon)
                    .placeholder(R.drawable.ic_downloading)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(categoryImage)
                categoryNameTv.text = category.name
            }

            binding.productsRecycler.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = productAdapter
            }
        }

        private fun onClick(listPosition: Int, productPosition: Int) {
            onItemClicked(listPosition, productPosition)
        }

        private fun onProductClicked(position: Int) {
            onClick(absoluteAdapterPosition, position)
        }
    }
}