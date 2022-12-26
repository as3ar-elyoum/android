package com.as3arelyoum.ui.home

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.as3arelyoum.R
import com.as3arelyoum.data.remote.dto.ProductDTO
import com.as3arelyoum.databinding.ProductCardBinding
import com.bumptech.glide.Glide

class ProductsAdapter(
    private val onItemClicked: (position: Int) -> Unit
) : RecyclerView.Adapter<ProductsAdapter.ProductViewHolder>() {

    var productList: List<ProductDTO> = ArrayList()

    @SuppressLint("NotifyDataSetChanged")
    fun setProducts(products: List<ProductDTO>) {
        productList = products
        notifyDataSetChanged()
    }

    override fun getItemCount() = productList.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.bind(product)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val recyclerCard =
            ProductCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(recyclerCard, onItemClicked)
    }


    inner class ProductViewHolder(
        val binding: ProductCardBinding,
        private val onItemClicked: (position: Int) -> Unit
    ) :
        ViewHolder(binding.root), View.OnClickListener {

        init {
            binding.root.setOnClickListener(this)
            binding.productDetailsBtn.setOnClickListener(this)
        }

        @SuppressLint("SetTextI18n")
        fun bind(product: ProductDTO) {
            Log.d("Product Is Binded", product.name)
            binding.apply {
                Glide.with(binding.root.context)
                    .load(product.image_url)
                    .placeholder(R.drawable.ic_downloading)
                    .into(productImage)
                nameTv.text = product.name
                priceTv.text = product.price + " " + "جنيه مصري"
                sourceTv.text = product.source
            }
        }

        override fun onClick(v: View?) {
            onItemClicked(absoluteAdapterPosition)
        }
    }

}
