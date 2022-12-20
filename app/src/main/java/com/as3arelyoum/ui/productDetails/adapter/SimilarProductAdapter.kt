package com.as3arelyoum.ui.productDetails.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.as3arelyoum.R
import com.as3arelyoum.data.remote.dto.ProductDTO
import com.as3arelyoum.databinding.SimilarProductCardBinding
import com.as3arelyoum.utils.helper.Constants.displayProductDetails
import com.bumptech.glide.Glide

class SimilarProductAdapter(
    private val onItemClicked: (position: Int) -> Unit
) : RecyclerView.Adapter<SimilarProductAdapter.CustomViewHolder>() {

    val similarProductsList = mutableListOf<ProductDTO>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val recyclerCard =
            SimilarProductCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomViewHolder(recyclerCard, onItemClicked)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setSimilarProductsList(products: List<ProductDTO>) {
        similarProductsList.clear()
        similarProductsList.addAll(products)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val productItems = similarProductsList[position]
        holder.binding.apply {
            Glide.with(holder.binding.root.context)
                .load(productItems.image_url)
                .into(productImage)
            nameTv.text = productItems.name
            priceTv.text =
                displayProductDetails(productItems.price, root.context.getString(R.string.egp))
            sourceTv.text = productItems.source
        }
    }

    override fun getItemCount(): Int {
        return similarProductsList.size
    }

    inner class CustomViewHolder(
        val binding: SimilarProductCardBinding,
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
