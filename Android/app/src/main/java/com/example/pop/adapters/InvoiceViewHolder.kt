package com.example.pop.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.pop.R
import com.example.webservice.Model.Invoice
import kotlinx.android.synthetic.main.invoice_list_item.view.*

class InvoiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(invoice : Invoice) {
        itemView.layoutReceiptItemTextName.text = invoice.Id.toString()
        itemView.layoutReceiptItemTextDate.text = invoice.DatumIzdavanja.toString()
       // itemView.layoutReceiptItemTextPrice.text = invoice.Cije
    }
}