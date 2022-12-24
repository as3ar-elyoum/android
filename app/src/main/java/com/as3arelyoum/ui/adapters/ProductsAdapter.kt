package com.as3arelyoum.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.as3arelyoum.R
import com.as3arelyoum.data.models.Product
import com.as3arelyoum.databinding.ProductCardBinding
import com.bumptech.glide.Glide
import java.util.*

class ProductsAdapter(
    private val onItemClicked: (position: Int) -> Unit
) : RecyclerView.Adapter<ProductsAdapter.CustomViewHolder>() {

    var productList: List<Product> = ArrayList()

    @SuppressLint("NotifyDataSetChanged")
    fun setProducts(products: List<Product>) {
        productList = products
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val recyclerCard =
            ProductCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomViewHolder(recyclerCard, onItemClicked)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val productItems = productList[position]
        holder.binding.apply {
            Glide.with(holder.binding.root.context)
                .load(productItems.image_url)
                .placeholder(R.drawable.ic_downloading)
                .into(productImage)
            nameTv.text = productItems.name
            priceTv.text = productItems.price + " " + "جنيه مصري"
            sourceTv.text = productItems.source
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    inner class CustomViewHolder(
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
