package com.example.pop.adapters

import android.content.Context
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.example.webservice.Model.Product
import kotlinx.android.synthetic.main.package_list_added_product.view.*

class AddedProductRecyclerAdapter(val context: Context?) : RecyclerView.Adapter<AddedProductViewHolder>(){

    private var products: ArrayList<Product> = ArrayList()
    lateinit var product: Product
    lateinit var activityContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddedProductViewHolder {
        return AddedProductViewHolder(LayoutInflater.from(parent.context).inflate(com.example.pop.R.layout.package_list_added_product, parent, false))
    }

    override fun onBindViewHolder(holder: AddedProductViewHolder, position: Int) {
        product = products[position]
        holder.bind(product)
        holder.itemView.btn_remove_product.setOnClickListener{removeProduct(position)}
        //activityContext = holder.itemView.btn_remove_product.context
    }

    private fun removeProduct(position: Int) {
        products.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, products.size)
    }

    fun submitList(data: List<Product>) {
        products.addAll(data)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return products.size
    }

}