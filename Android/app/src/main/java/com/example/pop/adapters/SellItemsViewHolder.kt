package com.example.pop.adapters

import android.text.Editable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.pop.SellItemsActivity
import com.example.webservice.Model.Item
import com.example.webservice.Model.PackageClass
import com.example.webservice.Model.Product
import kotlinx.android.synthetic.main.sell_item_list.view.*
import kotlinx.android.synthetic.main.activity_sell_items.*
import kotlinx.android.synthetic.main.activity_sell_items.view.*
import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode

const val INITIAL_QUANTITY = 1

class SellItemsViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

    private var quantityLimit : Int = 100
    private var parentActivity = itemView.context as SellItemsActivity

    fun bind(item : Item) {
        itemView.layoutSellItemListName.text = item.Naziv
        itemView.layoutSellItemListQuantity.text = INITIAL_QUANTITY.toString()

        if(item is PackageClass) quantityLimit = 100
        else if(item is Product) quantityLimit = item.Kolicina!!.toInt()
        updatePrice(item, 1)

        itemView.layoutSellItemListButtonDecrease.setOnClickListener {decreaseQuantity(itemView, item)}
        itemView.layoutSellItemListButtonIncrease.setOnClickListener {increaseQuantity(itemView, item)}
    }

    private fun decreaseQuantity(view: View, item: Item) {
        var quantity : Int = view.layoutSellItemListQuantity.text.toString().toInt()
        if(quantity > 1){
            quantity = quantity.dec()
            view.layoutSellItemListQuantity.text = quantity.toString()
            updatePrice(item, -1)
        }
    }

    private fun increaseQuantity(view: View, item: Item) {
        var quantity : Int = view.layoutSellItemListQuantity.text.toString().toInt()
        if(quantity < quantityLimit){
            quantity = quantity.inc()
            view.layoutSellItemListQuantity.text = quantity.toString()
            updatePrice(item, 1)
        }
    }

    private fun updatePrice(item: Item, change: Int) {
        if(item is PackageClass) {
            itemView.layoutSellItemListPrice.text = (BigDecimal(itemView.layoutSellItemListPrice.text.toString()) + BigDecimal(item.CijenaStavkeNakonPopusta)*BigDecimal(change)).toString() //Tu treba biti cjena paketa
            parentActivity.totalValue += item.CijenaStavkeNakonPopusta!!.toDouble() * change
            parentActivity.invoice_total_value.text = parentActivity.totalValue.toString()
            parentActivity.input_invoice_discount.setText("0")
        }

        else if(item is Product) {
            val value = itemView.layoutSellItemListPrice.text.toString().toDouble() + (item.Cijena!!.toDouble() * change)
            itemView.layoutSellItemListPrice.text = BigDecimal(value.toString()).toString()
            parentActivity.totalValue += item.Cijena!!.toDouble() * change
            parentActivity.invoice_total_value.text = parentActivity.totalValue.toString()
            parentActivity.input_invoice_discount.setText("0")
        }
    }

}