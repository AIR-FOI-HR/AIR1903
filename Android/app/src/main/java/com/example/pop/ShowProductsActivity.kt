package com.example.pop

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.pop.adapters.ProductRecyclerAdapter
import com.example.pop_sajamv2.Session
import com.example.webservice.Common.Common
import com.example.webservice.Model.Product
import com.example.webservice.Model.ProductResponse
import kotlinx.android.synthetic.main.activity_show_products.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable

class ShowProductsActivity : AppCompatActivity(){
    private lateinit var productAdapter: ProductRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_products)

        productAdapter = ProductRecyclerAdapter(applicationContext)
        product_list.adapter = productAdapter

        var productsApi = Common.api
        productsApi.getProducts(true, Session.user.Token, Session.user.KorisnickoIme).enqueue(object : Callback<ProductResponse> {
            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                Toast.makeText(this@ShowProductsActivity, t!!.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<ProductResponse>, response: Response<ProductResponse>) {
                var resp = response!!.body()!!.DATA
                if (response!!.body()!!.STATUSMESSAGE=="OLD TOKEN"){
                    var intent = Intent(this@ShowProductsActivity, LoginActivity::class.java)
                    Toast.makeText(this@ShowProductsActivity, "Sesija istekla, molimo prijavite se ponovno", Toast.LENGTH_LONG).show()
                    Session.reset()
                    startActivity(intent)
                    finishAffinity()
                }
                else if (response!!.body()!!.STATUSMESSAGE=="OK") {}
                else{
                    Toast.makeText(this@ShowProductsActivity, response.body()!!.STATUSMESSAGE, Toast.LENGTH_LONG).show()
                }
                if (resp!=null)
                    productAdapter.submitList(resp)
            }
        })

        btn_new_product.setOnClickListener{addProduct()}
        btn_new_package.setOnClickListener{createPackage()}
    }

    private fun addProduct(){
        val intent=Intent(applicationContext, ManageProductsActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("previousActivity", 1)
        applicationContext.startActivity(intent)
    }

    private fun createPackage() {
        val selectedProducts : List<Product> = productAdapter.getSelectedProducts()
        val intent = Intent(applicationContext, ManagePackagesActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("productList", selectedProducts as Serializable)
        applicationContext.startActivity(intent)
    }

    override fun onBackPressed() {
        super.onBackPressed()

        var intent = Intent(this@ShowProductsActivity, MainMenu::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        this@ShowProductsActivity.startActivity(intent)
        (this@ShowProductsActivity as Activity).overridePendingTransition(0,0)
        (this@ShowProductsActivity as Activity).finish()
        (this@ShowProductsActivity as Activity).overridePendingTransition(0,0)
    }


}
