package com.example.pop.adapters

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.example.pop.R
import kotlinx.android.synthetic.main.product_list_item.view.*
import com.squareup.picasso.Picasso
import com.example.webservice.Model.Product


class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    @SuppressLint("SetTextI18n")
    fun bind(product: Product) {

        val currency = " HRK"
        val expanded = product.isExpanded
        val selected = product.isSelected

        itemView.sub_item.visibility = if (expanded) View.VISIBLE else View.GONE
        itemView.img_selected_product.visibility = if (selected) View.VISIBLE else View.GONE

        val requestOptions = RequestOptions()
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)

        itemView.product_name.text = product.Naziv
        itemView.product_desc.text = product.Opis
        itemView.product_price.text = product.Cijena.toString() + " HRK"
        /*Glide.with(itemView.context)
            .applyDefaultRequestOptions(requestOptions)
            .load(product.picture)
            .into(itemView.product_image)*/
        Picasso.get().load(product.Slika).into(itemView.product_image)
    }

}

