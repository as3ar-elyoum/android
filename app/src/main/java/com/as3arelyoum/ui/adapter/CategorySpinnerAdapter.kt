package com.as3arelyoum.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.as3arelyoum.databinding.SpinnerItemsBinding

class CategorySpinnerAdapter(context: Context, product: List<Int>) :
    ArrayAdapter<Int>(context, 0, product) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, parent)
    }

    private fun initView(position: Int, parent: ViewGroup): View {
        val product = getItem(position)
        val binding = SpinnerItemsBinding.inflate(LayoutInflater.from(context), parent, false)
        binding.spinnerName.text = product.toString()
        return binding.root
    }
}
