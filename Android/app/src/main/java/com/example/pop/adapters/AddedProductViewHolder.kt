package com.example.pop.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.webservice.Model.*
import kotlinx.android.synthetic.main.package_list_added_product.view.*


class AddedProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(product: Product) {
        itemView.text_product_name.text = product.Naziv
        itemView.text_product_quantity.text = product.Kolicina
    }

}

