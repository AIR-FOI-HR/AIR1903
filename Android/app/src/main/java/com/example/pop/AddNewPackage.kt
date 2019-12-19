package com.example.pop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Spinner
import android.widget.Toast
import com.example.pop_sajamv2.Session
import com.example.webservice.Common.Common
import com.example.webservice.Model.NewPackageResponse
import com.example.webservice.Response.IMyAPI
import kotlinx.android.synthetic.main.activity_add_new_package.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddNewPackage : AppCompatActivity() {

    internal lateinit var mService: IMyAPI

    //var discounts = arrayOf("10", "20", "30" , "25", "15")

    //lateinit var discountForDatabase : String

    //lateinit var spinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_package)

        mService = Common.api

        if (addNewPackageButton!=null) {
            addNewPackageButton.setOnClickListener {
                addNewPackage(
                    newPackageName.text.toString(),
                    packageDesc.text.toString(),
                    productQuantity.text.toString(),
                    newPackageDiscount.text.toString()
                )
            }
        }
    }
    private fun addNewPackage(NazivPaketa: String, OpisPaketa: String, Kolicina: String, Popust: String) {
        mService.addNewPackage(Session.user.Token,true, NazivPaketa, OpisPaketa, Kolicina, Popust, Session.user.KorisnickoIme).enqueue(object:
            Callback<NewPackageResponse> {
            override fun onFailure(call: Call<NewPackageResponse>, t: Throwable) {
                Toast.makeText(this@AddNewPackage,t!!.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<NewPackageResponse>, response: Response<NewPackageResponse>) {

                Toast.makeText(this@AddNewPackage,"Paket uspjesno dodan!", Toast.LENGTH_SHORT).show()
                finish()

            }

        })

    }
}
