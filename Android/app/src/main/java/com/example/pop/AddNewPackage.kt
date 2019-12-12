package com.example.pop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Spinner
import com.example.webservice.Response.IMyAPI

class AddNewPackage : AppCompatActivity() {

    internal lateinit var mService: IMyAPI

    var discounts = arrayOf("10", "20", "30" , "25", "15")

    lateinit var discountForDatabase : String

    lateinit var spinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_package)
    }
}
