package com.example.pop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainMenu : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
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
