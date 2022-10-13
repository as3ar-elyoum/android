package com.as3arelyoum.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.as3arelyoum.data.model.Product
import com.as3arelyoum.databinding.ProductCardBinding

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
            postIdTv.text = productItems.postId.toString()
            nameTv.text = productItems.name
            emailTv.text = productItems.email
            bodyTv.text = productItems.body
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class CustomViewHolder(val binding: ProductCardBinding) :
        RecyclerView.ViewHolder(binding.root)
}
