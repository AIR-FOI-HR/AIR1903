package com.example.pop.adapters

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.pop.ManageProductsActivity
import com.example.webservice.Model.Product
import kotlinx.android.synthetic.main.product_list_item.view.*


class ProductRecyclerAdapter (val context: Context) : RecyclerView.Adapter<ProductViewHolder>(){

    private var products: ArrayList<Product> = ArrayList()
    lateinit var product:Product

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(LayoutInflater.from(parent.context).inflate(com.example.pop.R.layout.product_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {

        this.product = products[position]

        holder.bind(product)

        holder.itemView.setOnLongClickListener {
            val expanded = product.isExpanded
            product.isExpanded = !expanded
            notifyItemChanged(position)
            true
        }

        holder.itemView.img_delete_product.setOnClickListener{displayText("DELETE")}
        holder.itemView.img_edit_product.setOnClickListener{editProduct()}

    }

    private fun displayText(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    private fun editProduct(){
        val intent=Intent(this.context,ManageProductsActivity::class.java)
        intent.putExtra("product",product)
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)

    }

    override fun getItemCount(): Int {
        return products.size
    }

    fun submitList(data: List<Product>){
        products.addAll(data)
        notifyDataSetChanged()
    }


}
