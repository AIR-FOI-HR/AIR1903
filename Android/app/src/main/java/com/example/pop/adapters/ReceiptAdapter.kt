package com.example.pop.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pop.R

class ReceiptAdapter : RecyclerView.Adapter<ReceiptViewHolder>() {
    var data = listOf<Receipt>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ReceiptViewHolder, position: Int) {
        val item = data[position]
        holder.bind()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceiptViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.receipt_list_item, parent, false)
        return ReceiptViewHolder(view)
    }
}