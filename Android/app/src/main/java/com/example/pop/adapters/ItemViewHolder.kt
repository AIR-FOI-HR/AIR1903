package com.example.pop.adapters

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_list.view.*
import com.squareup.picasso.Picasso
import com.example.webservice.Model.*


class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener{

    private var mClickListener: ItemClickListener? = null


    @SuppressLint("SetTextI18n")
    fun bind(
        item: Item,
        _clickListener: ItemClickListener?
    ) {
        mClickListener = _clickListener
        val currency = " HRK"
        val expanded = item.expanded
        val selected = item.selected

        itemView.item_expansion.visibility = if (expanded) View.VISIBLE else View.GONE
        itemView.img_selected_item.visibility = if (selected) View.VISIBLE else View.GONE

        itemView.item_name.text = item.Naziv
        itemView.item_desc.text = item.Opis

        if(item is Product){
            itemView.item_value.text = "%.2f".format(item.Cijena!!.toDouble()) + currency
            itemView.item_quantity.text = item.Kolicina
        }
        else if(item is PackageClass){
            itemView.item_value.text = item.Popust.toString() + " %"
            itemView.item_quantity.text = item.Kolicina
            itemView.package_price.text=item.CijenaStavkeNakonPopusta
        }

        itemView.setOnClickListener(this)
        itemView.setOnLongClickListener(this)

        Picasso.get().load(item.Slika).into(itemView.item_image)
    }

    override fun onClick(view: View) {
        if (mClickListener != null) mClickListener?.onItemClick(view, adapterPosition)
    }

    override fun onLongClick(view: View?): Boolean {
        if (mClickListener != null) mClickListener?.onItemLongClick(view, adapterPosition)
        return true
    }
}

