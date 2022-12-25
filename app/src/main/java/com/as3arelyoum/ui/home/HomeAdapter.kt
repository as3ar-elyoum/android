package com.as3arelyoum.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.as3arelyoum.R
import com.as3arelyoum.data.remote.dto.ProductDTO
import com.as3arelyoum.databinding.HomeProductCardBinding
import com.bumptech.glide.Glide

class HomeAdapter(
    private val onItemClicked: (position: Int) -> Unit
) : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    var productList: List<ProductDTO> = ArrayList()

    @SuppressLint("NotifyDataSetChanged")
    fun setProducts(products: List<ProductDTO>) {
        productList = products
        notifyDataSetChanged()
    }

    override fun getItemCount() = productList.size

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val product = productList[position]
        holder.bind(product)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val recyclerCard =
            HomeProductCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(recyclerCard, onItemClicked)
    }


    inner class HomeViewHolder(
        val binding: HomeProductCardBinding,
        private val onItemClicked: (position: Int) -> Unit
    ) :
        ViewHolder(binding.root), View.OnClickListener {

        init {
            binding.root.setOnClickListener(this)
            binding.productDetailsBtn.setOnClickListener(this)
        }

        @SuppressLint("SetTextI18n")
        fun bind(product: ProductDTO) {
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