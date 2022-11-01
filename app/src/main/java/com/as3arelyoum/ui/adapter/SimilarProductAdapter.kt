package com.as3arelyoum.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.as3arelyoum.R
import com.as3arelyoum.data.model.Product
import com.as3arelyoum.databinding.SimilarProductCardBinding
import com.as3arelyoum.utils.Constants.displayProductDetails
import com.bumptech.glide.Glide

class SimilarProductAdapter(
    private var similarProductsList: List<Product>,
    private val onItemClicked: (position: Int) -> Unit
) : RecyclerView.Adapter<SimilarProductAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val recyclerCard =
            SimilarProductCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomViewHolder(recyclerCard, onItemClicked)
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
            sourceTv.text = displayProductDetails(productItems.source, root.context.getString(R.string.from))
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
            binding.root.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            onItemClicked(absoluteAdapterPosition)
        }
    }
}
