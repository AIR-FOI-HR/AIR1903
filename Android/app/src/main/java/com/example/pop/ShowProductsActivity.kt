package com.example.pop

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.pop.adapters.ProductRecyclerAdapter
import com.example.pop_sajamv2.Session
import com.example.webservice.Common.Common
import com.example.webservice.Model.ProductResponse
import kotlinx.android.synthetic.main.activity_show_products.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShowProductsActivity : AppCompatActivity(){
    private lateinit var productAdapter: ProductRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_products)

        productAdapter = ProductRecyclerAdapter(applicationContext)
        product_list.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        product_list.adapter = productAdapter

        var productsApi = Common.api
        productsApi.getProducts(true, Session.user.Token).enqueue(object : Callback<ProductResponse> {
            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                Toast.makeText(this@ShowProductsActivity, t!!.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<ProductResponse>, response: Response<ProductResponse>) {
                var resp = response!!.body()!!.DATA
                println(Session.user.Token)
                if (response!!.body()!!.STATUSMESSAGE=="OLD TOKEN"){
                    var intent = Intent(this@ShowProductsActivity, LoginActivity::class.java)
                    Toast.makeText(this@ShowProductsActivity, "Sesija istekla, molimo prijavite se ponovno", Toast.LENGTH_LONG).show()
                    Session.reset()
                    startActivity(intent)
                }
                if (resp!=null)
                    productAdapter.submitList(resp)
            }
        })
    }
}
