package com.example.pop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.database.Entities.Product
import com.squareup.picasso.Picasso

class ShowProductsActivity : AppCompatActivity() {

    private lateinit var productAdapter: ProductsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_products)
    }


    inner class ProductsAdapter : RecyclerView.Adapter<ProductsAdapter.ProductViewHolder> (){

        private val products: MutableList<Product> = mutableListOf()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
            return ProductViewHolder(layoutInflater.inflate(R.layout.product_item_layout, parent, false))
        }

        override fun getItemCount(): Int {
            return products.size
        }

        override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
            holder.bindModel(products[position])
        }

        fun setProducts(data: List<Product>){
            products.addAll(data)
            notifyDataSetChanged()
        }

        inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

            val productName : TextView = itemView.findViewById(R.id.proizvodNaziv)
            val productPrice : TextView = itemView.findViewById(R.id.proizvodCijena)
            val productDescription : TextView = itemView.findViewById(R.id.proizvodOpis)
            val productImage : ImageView = itemView.findViewById(R.id.proizvodSlika)

            fun bindModel(product: Product){
                productName.text = product.Naziv
                productPrice.text = product.Cijena
                productDescription.text = product.Opis
                Picasso.get().load(product.Slika).into(productImage)
            }
        }
    }
}
