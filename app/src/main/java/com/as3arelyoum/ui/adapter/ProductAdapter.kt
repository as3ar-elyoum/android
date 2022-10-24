package com.as3arelyoum.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.as3arelyoum.data.model.Product
import com.as3arelyoum.databinding.ProductCardBinding
import com.bumptech.glide.Glide

class ProductAdapter(
    private var list: List<Product>
) : RecyclerView.Adapter<ProductAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val recyclerCard =
            ProductCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomViewHolder(recyclerCard)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val productItems = list[position]
        holder.binding.apply {
            Glide.with(holder.binding.root.context)
                .load(productItems.image_url)
                .into(productImage)
            nameTv.text = productItems.name
            priceTv.text = productItems.price
            productSourcePage.text = productItems.source_page
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class CustomViewHolder(val binding: ProductCardBinding) :
        RecyclerView.ViewHolder(binding.root)
}
