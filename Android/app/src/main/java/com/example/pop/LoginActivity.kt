package com.example.pop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.webservice.Common.Common
import com.example.webservice.Model.ApiResponse
import com.example.webservice.Response.IMyAPI
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    internal lateinit var mService:IMyAPI


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mService = Common.api


        //layoutLoginButtonRegister.setOnClickListener{startActivity(Intent(this@LoginActivity,Registraacija::class.java))}
        layoutLoginButtonLogin.setOnClickListener{authenticateUser(layoutLoginInputUsername.text.toString(),layoutLoginInputPassword.text.toString())}
    }
        private fun authenticateUser(KorisnickoIme: String, Lozinka: String) {
            mService.storeUser(KorisnickoIme, Lozinka).enqueue(object : Callback<ApiResponse> {
                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                    Toast.makeText(this@LoginActivity, t!!.message, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                    if (!response!!.body()!!.STATUS)
                        Toast.makeText(
                            this@LoginActivity,
                            response!!.body()!!.STATUSMESSAGE,
                            Toast.LENGTH_SHORT
                        ).show()
                    else {
                        Toast.makeText(this@LoginActivity, "Uspjesna prijava", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            })

        }
}
