package com.example.pop.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.webservice.Model.Item
import com.example.webservice.Model.PackageClass
import com.example.webservice.Model.Product
import kotlinx.android.synthetic.main.sell_item_list.view.*

const val INITIAL_QUANTITY = 1

class SellItemsViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
    private var quantityLimit : Int = 10000
    fun bind(item : Item) {
        itemView.layoutSellItemListName.text = item.Naziv
        itemView.layoutSellItemListQuantity.text = INITIAL_QUANTITY.toString()

        if(item is PackageClass) quantityLimit = item.Kolicina!!.toInt()
        else if(item is Product) quantityLimit = item.Kolicina.toInt()
        updatePrice(item)

        itemView.layoutSellItemListButtonDecrease.setOnClickListener {
            decreaseQuantity(itemView)
            updatePrice(item)
        }
        itemView.layoutSellItemListButtonIncrease.setOnClickListener {
            increaseQuantity(itemView)
            updatePrice(item)
        }
    }

    private fun decreaseQuantity(view : View) {
        var quantity : Int = view.layoutSellItemListQuantity.text.toString().toInt()
        if(quantity > 0) quantity -= 1
        view.layoutSellItemListQuantity.text = quantity.toString()
    }

    private fun increaseQuantity(view: View) {
        var quantity : Int = view.layoutSellItemListQuantity.text.toString().toInt()
        if(quantity < quantityLimit) quantity += 1
        view.layoutSellItemListQuantity.text = quantity.toString()
    }

    private fun updatePrice(item : Item) {
        if(item is PackageClass) {
            itemView.layoutSellItemListPrice.text = item.Popust //Tu treba biti cjena paketa
        }
        else if(item is Product) {
            itemView.layoutSellItemListPrice.text = (item.Cijena.toInt() * itemView.layoutSellItemListQuantity.text.toString().toInt()).toString()
        }
    }
}