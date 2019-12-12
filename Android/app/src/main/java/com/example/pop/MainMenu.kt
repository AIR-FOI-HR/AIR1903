package com.example.pop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.webservice.Common.Common
import com.example.webservice.Model.Product
import com.example.webservice.Response.IMyAPI
import kotlinx.android.synthetic.main.activity_main_menu.*

class MainMenu : AppCompatActivity() {
    internal lateinit var mService: IMyAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
        mService = Common.api

        showAllProductsButton.setOnClickListener{showProducts()}
        addProductButton.setOnClickListener{addNewProduct()}
        editProductButton.setOnClickListener { editProduct() }
    }
    private fun showProducts(){
        val intent = Intent(this, ShowProductsActivity::class.java)
        startActivity(intent)
    }
    private fun addNewProduct(){
        val intent = Intent(this, ManageProductsActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("previousActivity", 2)
        startActivity(intent)
    }

    private fun editProduct(){
        val testProduct : Product = Product(1234, "Foi Product", 14.0, "Test product", "")

        val intent = Intent(this, ManageProductsActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("product", testProduct)
        intent.putExtra("previousActivity", 2)
        startActivity(intent)
    }
    private fun addNewPackage(){
        
    }
}
