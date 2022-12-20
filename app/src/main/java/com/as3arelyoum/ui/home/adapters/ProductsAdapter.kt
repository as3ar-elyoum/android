package com.as3arelyoum.ui.home.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.as3arelyoum.R
import com.as3arelyoum.data.remote.dto.ProductDTO
import com.as3arelyoum.databinding.ProductCardBinding
import com.bumptech.glide.Glide
import java.util.*

class ProductsAdapter(
    private val onItemClicked: (position: Int) -> Unit
) : RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder>() {

    val productList = mutableListOf<ProductDTO>()

    fun setProductsList(products: List<ProductDTO>) {
        productList.clear()
        productList.addAll(products)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val recyclerCard =
            ProductCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductsViewHolder(recyclerCard, onItemClicked)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        val productItems = productList[position]
        holder.binding.apply {
            Glide.with(root.context)
                .load(productItems.image_url)
                .placeholder(R.drawable.ic_downloading)
                .into(productImage)
            nameTv.text = productItems.name
            priceTv.text = productItems.price + " " + "جنيه مصري"
            sourceTv.text = productItems.source
        }
    }

    override fun getItemCount() = productList.size

    inner class ProductsViewHolder(
        val binding: ProductCardBinding,
        private val onItemClicked: (position: Int) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            binding.root.setOnClickListener(this)
            binding.productDetailsBtn.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            onItemClicked(absoluteAdapterPosition)
        }
    }
}