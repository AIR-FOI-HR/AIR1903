package com.example.pop.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.webservice.Model.Product
import kotlinx.android.synthetic.main.package_list_product.view.*

class ProductRecyclerAdapter(val context: Context?) : RecyclerView.Adapter<ProductViewHolder>(){

    var products: ArrayList<Product> = ArrayList()
    lateinit var product: Product

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(LayoutInflater.from(parent.context).inflate(com.example.pop.R.layout.package_list_product, parent, false))
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        product = products[position]
        holder.bind(product)
        holder.itemView.btn_add_products.setOnClickListener{
            val newQuantity: String = (holder.itemView.product_quantity.text.toString().toInt().inc()).toString()
            holder.itemView.product_quantity.text = newQuantity
            products[position].Kolicina = newQuantity
        }

        holder.itemView.btn_decrease_products.setOnClickListener{
            if(holder.itemView.product_quantity.text.toString() != "0") {
                val newQuantity: String = (holder.itemView.product_quantity.text.toString().toInt().dec()).toString()
                holder.itemView.product_quantity.text = newQuantity
                products[position].Kolicina = newQuantity
            }
        }
    }

    fun submitList(data: List<Product>){
        products.addAll(data)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return products.size
    }

}