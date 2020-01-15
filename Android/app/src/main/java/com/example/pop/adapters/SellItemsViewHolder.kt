package com.example.pop.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.webservice.Model.Item
import com.example.webservice.Model.PackageClass
import com.example.webservice.Model.Product
import kotlinx.android.synthetic.main.sell_item_list.view.*

class SellItemsViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
    fun bind(item : Item) {
        itemView.layoutSellItemListName.text = item.Naziv

        //Ovo treba sredit
        if(item is PackageClass) itemView.layoutSellItemListQuantity.text = item.Kolicina
        if(item is Product) itemView.layoutSellItemListQuantity.text = item.Kolicina

        itemView.layoutSellItemListButtonDecrease.setOnClickListener { decreaseQuantity(itemView) }
        itemView.layoutSellItemListButtonIncrease.setOnClickListener { increaseQuantity(itemView) }
    }

    private fun decreaseQuantity(view : View) {
        var quantity : Int = view.layoutSellItemListQuantity.text.toString().toInt()
        if(quantity > 0) quantity -= 1
        view.layoutSellItemListQuantity.text = quantity.toString()
    }

    private fun increaseQuantity(view: View) {
        var quantity : Int = view.layoutSellItemListQuantity.text.toString().toInt()
        if(quantity < 100000) quantity += 1
        view.layoutSellItemListQuantity.text = quantity.toString()
    }
}