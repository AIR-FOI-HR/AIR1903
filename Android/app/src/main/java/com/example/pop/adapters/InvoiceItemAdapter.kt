package com.example.pop.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pop.R
import com.example.webservice.Model.Item

class InvoiceItemAdapter : RecyclerView.Adapter<InvoiceItemViewHolder>() {
    var data = listOf<Item>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InvoiceItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.invoice_items_list, parent, false)
        return InvoiceItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: InvoiceItemViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    override fun getItemCount() = data.size
}