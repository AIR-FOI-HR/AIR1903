package com.example.pop.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.webservice.Model.Item
import com.example.webservice.Model.PackageClass
import com.example.webservice.Model.Product
import kotlinx.android.synthetic.main.invoice_items_list.view.*

class InvoiceItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(item : Item) {
        itemView.layoutInvoiceItemsListName.text = item.Naziv
        if(item is Product) {
            itemView.layoutInvoiceItemsListQuantity.text = item.Kolicina
            itemView.layoutInvoiceItemsListPrice.text = item.Cijena
            itemView.layoutInvoiceItemsListTotal.text = item.CijenaStavke
        }
        else if(item is PackageClass) {
            itemView.layoutInvoiceItemsListQuantity.text = item.Kolicina
            itemView.layoutInvoiceItemsListPrice.text = item.CijenaNakonPopusta
            itemView.layoutInvoiceItemsListTotal.text = item.CijenaStavkeNakonPopusta
        }
    }
}