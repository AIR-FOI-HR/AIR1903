package com.example.pop.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pop.R
import com.example.webservice.Model.Store

interface StoreClickListener {
    fun onStoreClick(view: View, position: Int)
}

class StoreRecyclerAdapter(val context: Context?) : RecyclerView.Adapter<StoreViewHolder>(){

    private var stores: ArrayList<Store> = ArrayList()
    private var mClickListener: StoreClickListener? = null
    private var mInflater: LayoutInflater? = null
    lateinit var store: Store
    lateinit var activityContext: Context

    init {
        mInflater = LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreViewHolder {
        val view = mInflater!!.inflate(R.layout.store_list_item, parent, false)
        return StoreViewHolder(view)
    }

    override fun onBindViewHolder(holder: StoreViewHolder, position: Int) {
        val store = stores[position]
        holder.bind(store, mClickListener)
    }

    fun setClickListener(storeClickListener: StoreClickListener) {
        mClickListener = storeClickListener
    }

    fun getStore(position: Int): Store? {
        return stores[position]
    }

    fun submitList(data: ArrayList<Store>){
        stores.addAll(data)
        Log.e("LISTA: ", stores[0].Naziv + " " + stores[1].Naziv)
        notifyDataSetChanged()
        Log.e("LISTA: ", stores[0].Naziv + " " + stores[1].Naziv)
    }

    override fun getItemCount(): Int {
        return stores.size
    }

}