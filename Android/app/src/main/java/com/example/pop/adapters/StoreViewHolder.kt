package com.example.pop.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.webservice.Model.Store
import kotlinx.android.synthetic.main.store_list_item.view.*

class StoreViewHolder(storeView: View) : RecyclerView.ViewHolder(storeView), View.OnClickListener {
    private var mClickListener: StoreClickListener? = null


    fun bind(
        store: Store,
        _clickListener: StoreClickListener?
    ) {
        mClickListener = _clickListener
        itemView.text_store_name.text = store.Naziv
        val selected = store.selected
        itemView.setOnClickListener(this)
        itemView.img_selected_store.visibility = if (selected) View.VISIBLE else View.GONE
    }

    override fun onClick(view: View) {
        if (mClickListener != null) mClickListener?.onStoreClick(view, adapterPosition)
    }
}