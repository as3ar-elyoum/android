package com.as3arelyoum.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.as3arelyoum.data.model.Product
import com.as3arelyoum.databinding.SpinnerItemsBinding

class StatusSpinnerAdapter(context: Context, product: List<Product>) :
    ArrayAdapter<Product>(context, 0, product) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, parent)
    }

    private fun initView(position: Int, parent: ViewGroup): View {
        val product = getItem(position)
        val binding = SpinnerItemsBinding.inflate(LayoutInflater.from(context), parent, false)

        binding.apply {
            spinnerName.text = product?.status
            binding.spinnerImage.visibility = View.GONE
        }

        return binding.root
    }

    fun setStatus(status: List<Product>) {
        addAll(status)
        notifyDataSetChanged()
    }
}
