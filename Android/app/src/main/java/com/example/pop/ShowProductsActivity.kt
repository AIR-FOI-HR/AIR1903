package com.example.pop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.webservice.Model.ProductResponse
import com.example.pop.adapters.ProductRecyclerAdapter
import com.example.webservice.Common.Common
import kotlinx.android.synthetic.main.activity_show_products.*
import retrofit2.*
import kotlinx.android.synthetic.main.product_list_item.*

class ShowProductsActivity : AppCompatActivity(){
    private lateinit var productAdapter: ProductRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_products)

        productAdapter = ProductRecyclerAdapter(applicationContext)
        product_list.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        product_list.adapter = productAdapter

        var productsApi = Common.api

        productsApi.getProducts().enqueue(object : Callback<ProductResponse> {
            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                Toast.makeText(this@ShowProductsActivity, t!!.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<ProductResponse>, response: Response<ProductResponse>) {
                var resp = response!!.body()!!.DATA!!
                productAdapter.submitList(resp)
            }
        })
    }
}
