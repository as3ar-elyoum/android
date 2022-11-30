package com.as3arelyoum.ui.product.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.as3arelyoum.R
import com.as3arelyoum.data.remote.dto.ProductDTO
import com.as3arelyoum.databinding.ProductCardBinding
import com.bumptech.glide.Glide

class ProductsAdapter(
    private val onItemClicked: (position: Int) -> Unit
) : RecyclerView.Adapter<ProductsAdapter.CustomViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<ProductDTO>() {
        override fun areItemsTheSame(oldItem: ProductDTO, newItem: ProductDTO): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductDTO, newItem: ProductDTO): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val recyclerCard =
            ProductCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomViewHolder(recyclerCard, onItemClicked)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val productItems = differ.currentList[position]
        holder.binding.apply {
            Glide.with(holder.binding.root.context)
                .load(productItems.image_url)
                .placeholder(R.drawable.ic_downloading)
                .into(productImage)
            nameTv.text = "${productItems.id} - ${productItems.name}"
            priceTv.text =  productItems.price + " " + "جنيه مصري"
            sourceTv.text = "من" + " " + productItems.source
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class CustomViewHolder(
        val binding: ProductCardBinding,
        private val onItemClicked: (position: Int) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            binding.productCard.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            onItemClicked(absoluteAdapterPosition)
        }
    }
}
