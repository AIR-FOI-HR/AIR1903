package com.example.pop.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.webservice.Model.*
import kotlinx.android.synthetic.main.package_list_product.view.*
import kotlinx.android.synthetic.main.package_list_product.view.text_product_name


class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(product: Product) {
        itemView.text_product_name.text = product.Naziv
        itemView.product_quantity.text = product.Kolicina
    }

}

