package com.example.pop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.webservice.Model.NewProductResponse
import com.example.webservice.Response.IMyAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddNewProduct : AppCompatActivity() {
    internal lateinit var mService: IMyAPI


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_product)
    }

    private fun addNewProduct(Naziv: String, Opis: String, Cijena: String, Slika: String) {

    }
}
