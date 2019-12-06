package com.example.pop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pop.R
import com.example.webservice.Common.Common
import com.example.webservice.Response.IMyAPI
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main_menu.*

class MainMenu : AppCompatActivity() {
    internal lateinit var mService: IMyAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
        mService = Common.api

        showAllProductsButton.setOnClickListener{showProducts()}
        addProductButton.setOnClickListener{addNewProduct()}
    }
    private fun showProducts(){
        val intent = Intent(this, ShowProductsActivity::class.java)
        startActivity(intent)
    }
    private fun addNewProduct(){
        val intent = Intent(this, AddNewProduct::class.java)
        startActivity(intent)
    }
}
