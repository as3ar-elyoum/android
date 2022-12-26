package com.as3arelyoum.ui.home

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.as3arelyoum.data.remote.dto.CategoryDTO
import com.as3arelyoum.data.remote.dto.ProductDTO
import com.as3arelyoum.databinding.HomeCardBinding

class HomeAdapter(private val onItemClicked: (position: Int, position2: Int) -> Unit) :
    RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {
    var productLists: List<List<ProductDTO>> = ArrayList()
    var categoriesList: List<CategoryDTO> = ArrayList()

    fun setCategories(categories: List<CategoryDTO>) {
        categoriesList = categories
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setProductsLists(products: List<List<ProductDTO>>) {
        productLists = products
        notifyDataSetChanged()
    }

    override fun getItemCount() = productLists.size

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val products = productLists[position]
        holder.bind(products)
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
        fun bind(products: List<ProductDTO>) {
            val productAdapter = ProductsAdapter { position -> onProductClicked(position) }
            productAdapter.setProducts(products)

            val categoryId = products.first().category_id
            val category = categoriesList.find { category -> category.id == categoryId }
            binding.categoryNameTv.text = category?.name

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