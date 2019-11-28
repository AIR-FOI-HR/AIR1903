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
import com.example.database.Entities.ProductResponse
import com.example.webservice.Response.ProductApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_show_products.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
class ShowProductsActivity : AppCompatActivity() {

    private lateinit var productAdapter: ProductsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_products)

        productAdapter = ProductsAdapter()
        lista_proizvoda.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        lista_proizvoda.adapter = productAdapter
        val retrofit : Retrofit = Retrofit.Builder()
            .baseUrl("http://cortex.foi.hr")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        val productsApi = retrofit.create(ProductApi::class.java)

        productsApi.getProducts()
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ productAdapter.setProducts(it.data) },
                {
                    Toast.makeText(applicationContext, it.message, Toast.LENGTH_SHORT).show()
                })
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
