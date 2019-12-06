package com.example.pop.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.pop.ManageProductsActivity
import com.example.webservice.Model.Product
import com.example.pop.R
import com.example.pop.RegistrationActivity
import com.example.pop.ShowProductsActivity
import kotlinx.android.synthetic.main.product_list_item.view.*
import kotlin.collections.ArrayList

class ProductRecyclerAdapter (val context: Context) : RecyclerView.Adapter<ProductViewHolder>(){

    private var products: ArrayList<Product> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.product_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {

        val product = products[position]

        holder.bind(product)

        holder.itemView.setOnLongClickListener {
            val expanded = product.isExpanded
            product.isExpanded = !expanded
            notifyItemChanged(position)
            true
        }

        holder.itemView.img_edit_product.setOnClickListener{displayText("EDIT")}
        holder.itemView.img_delete_product.setOnClickListener{displayText("DELETE")}

    }

    private fun displayText(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    override fun getItemCount(): Int {
        return products.size
    }

    fun submitList(data: List<Product>){
        products.addAll(data)
        notifyDataSetChanged()
    }


}
