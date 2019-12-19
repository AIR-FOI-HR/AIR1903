package com.example.pop.adapters

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.example.pop.R
import kotlinx.android.synthetic.main.item_list.view.*
import com.squareup.picasso.Picasso
import com.example.webservice.Model.*


class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    @SuppressLint("SetTextI18n")
    fun bind(item: Item) {

        val currency = " HRK"
        val expanded = item.expanded
        val selected = item.selected

        itemView.item_expansion.visibility = if (expanded) View.VISIBLE else View.GONE
        itemView.img_selected_item.visibility = if (selected) View.VISIBLE else View.GONE

        val requestOptions = RequestOptions()
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)

        itemView.item_name.text = item.Naziv
        itemView.item_desc.text = item.Opis

        if(item is Product){
            itemView.item_value.text = item.Cijena + currency
            itemView.item_quantity.text = item.Kolicina
        }

        else if(item is Package){
            itemView.item_value.text = item.Popust.toString()
            itemView.item_quantity.text = item.KolicinaPaketa

        }



        Picasso.get().load(item.Slika).into(itemView.item_image)
    }

}

