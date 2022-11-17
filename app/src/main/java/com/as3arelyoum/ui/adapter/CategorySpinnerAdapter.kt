package com.as3arelyoum.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.as3arelyoum.data.model.Category
import com.as3arelyoum.databinding.SpinnerItemsBinding
import com.bumptech.glide.Glide

class CategorySpinnerAdapter(context: Context, product: List<Category>) :
    ArrayAdapter<Category>(context, 0, product) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, parent)
    }

    private fun initView(position: Int, parent: ViewGroup): View {
        val category = getItem(position)
        val binding = SpinnerItemsBinding.inflate(LayoutInflater.from(context), parent, false)

        binding.apply {
            Glide.with(context)
                .load(category?.icon)
                .into(spinnerImage)
            spinnerName.text = category?.name
        }
        return binding.root
    }
}
