package com.example.pop.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pop.R

class InvoiceAdapter : RecyclerView.Adapter<InvoiceViewHolder>() {
    var data = listOf<Int/*Receipt*/>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: InvoiceViewHolder, position: Int) {
        val item = data[position]
        holder.bind()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InvoiceViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.invoice_list_item, parent, false)
        return InvoiceViewHolder(view)
    }
}