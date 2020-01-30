package com.example.pop.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pop.R
import com.example.webservice.Model.Item


class SellItemsAdapter : RecyclerView.Adapter<SellItemsViewHolder>(){
    var data = listOf<Item>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SellItemsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.sell_item_list, parent, false)

        return SellItemsViewHolder(view)
    }


    override fun onBindViewHolder(holder: SellItemsViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    fun getIds():ArrayList<Int> {
        var ids = ArrayList<Int>()
        for (i: Item in data) {
            ids.add(i.Id!!)
        }
        return ids
    }
}