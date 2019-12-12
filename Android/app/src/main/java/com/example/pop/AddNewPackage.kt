package com.example.pop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Spinner
import android.widget.Toast
import com.example.pop_sajamv2.Session
import com.example.webservice.Common.Common
import com.example.webservice.Model.NewPackageResponse
import com.example.webservice.Response.IMyAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddNewPackage : AppCompatActivity() {

    internal lateinit var mService: IMyAPI

    var discounts = arrayOf("10", "20", "30" , "25", "15")

    lateinit var discountForDatabase : String

    lateinit var spinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_package)

        mService = Common.api
    }
    private fun addNewPackage(NazivPaketa: String, Id_Proizvoda: String, Kolicina: String, Popust: String) {
        

    }
}
