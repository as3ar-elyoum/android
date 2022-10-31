package com.as3arelyoum.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.as3arelyoum.data.model.Product
import com.as3arelyoum.databinding.SimilarProductCardBinding
import com.bumptech.glide.Glide

class SimilarProductAdapter(
    private var list: List<Product>,
    private val onItemClicked: (position: Int) -> Unit
) : RecyclerView.Adapter<SimilarProductAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val recyclerCard =
            SimilarProductCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomViewHolder(recyclerCard, onItemClicked)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val productItems = list[position]
        holder.binding.apply {
            Glide.with(holder.binding.root.context)
                .load(productItems.image_url)
                .into(productImage)
            nameTv.text = productItems.name
            priceTv.text =  productItems.price + " " + "جنيه مصري"
            sourceTv.text = "من" + " " + productItems.source
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class CustomViewHolder(
        val binding: SimilarProductCardBinding,
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
