package com.example.pop.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.database.Entities.Product
import com.example.pop.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.product_list_item.view.*
import org.w3c.dom.Text

class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val productName : TextView = itemView.findViewById(R.id.productName)
    val productPrice : TextView = itemView.findViewById(R.id.productPrice)
    val productDescription : TextView = itemView.findViewById(R.id.productDescription)
    val productImage : ImageView = itemView.findViewById(R.id.productImage)

    fun bind(product: Product) {
        /*
        val requestOptions = RequestOptions()
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)
        
        Glide.with(itemView.context)
            .applyDefaultRequestOptions(requestOptions)
            .load(product.picture)
            .into(itemView.product_image)
        */
        productName.text = product.Naziv
        productPrice.text = product.Cijena
        productDescription.text = product.Opis
        Picasso.get().load(product.Slika).into(productImage)

        /*
        itemView.product_name.text = product.name
        itemView.product_desc.text = product.description
        itemView.product_price.text = product.price.toString()
         */
    }

}
