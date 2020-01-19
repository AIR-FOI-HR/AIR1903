package com.example.pop.adapters

import android.content.Intent
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.pop.InvoiceDetailsActivity
import com.example.pop.R
import com.example.webservice.Model.Invoice
import kotlinx.android.synthetic.main.invoice_list_item.view.*

class InvoiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(invoice : Invoice) {
        itemView.text_invoice_id.text = invoice.Id.toString()
        itemView.text_invoice_date.text = invoice.DatumIzdavanja
        itemView.text_invoice_total.text = invoice.Popust.toString()

        itemView.setOnClickListener {
            val intent = Intent(it.context, InvoiceDetailsActivity::class.java)
            intent.putExtra("invoice", invoice)
            startActivity(it.context, intent, null)
        }
    }
}