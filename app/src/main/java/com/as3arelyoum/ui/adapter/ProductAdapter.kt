package com.as3arelyoum.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils.loadAnimation
import androidx.recyclerview.widget.RecyclerView
import com.as3arelyoum.R
import com.as3arelyoum.data.model.Product
import com.as3arelyoum.databinding.ProductCardBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class ProductAdapter(
    private var context: Context,
    private var list: List<Product>
) : RecyclerView.Adapter<ProductAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val recyclerCard =
            ProductCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomViewHolder(recyclerCard)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val categoryItems = list[position]
        holder.binding.apply {
            Glide.with(context)
                .load(R.mipmap.ic_launcher_round)
                .placeholder(android.R.drawable.stat_sys_download)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(productImage)

            productName.text = categoryItems.name
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class CustomViewHolder(val binding: ProductCardBinding) :
        RecyclerView.ViewHolder(binding.root)
}
