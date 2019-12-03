package com.example.pop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.pop.adapters.ProductRecyclerAdapter
import com.example.webservice.Response.ProductApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_show_products.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class ShowProductsActivity : AppCompatActivity(){
    private lateinit var productAdapter: ProductRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_products)

        productAdapter = ProductRecyclerAdapter()
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
            .subscribe({ productAdapter.submitList(it.DATA) },
                {
                    Toast.makeText(applicationContext, it.message, Toast.LENGTH_SHORT).show()
                })
    }

    /*
    private fun addDataSet(){
        val data = DataSource.createDataSet()
        blogAdapter.submitList(data)
    }

    private fun initRecyclerView(){

        recycler_view.apply {
            layoutManager = LinearLayoutManager(this@ShowProductsActivity)
            val topSpacingDecorator = TopSpacingItemDecoration(30)
            addItemDecoration(topSpacingDecorator)
            blogAdapter = ProductRecyclerAdapter()
            adapter = blogAdapter
        }
    }
     */
}
