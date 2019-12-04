package com.example.pop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.pop_sajamv2.Session
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


        layoutLoginButtonLogin.setOnClickListener{authenticateUser(layoutLoginInputUsername.text.toString(),layoutLoginInputPassword.text.toString())}
        layoutLoginButtonRegister.setOnClickListener{startActivity(Intent(this@LoginActivity,RegistrationActivity::class.java))}
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
                        Toast.makeText(this@LoginActivity, R.string.toastLoginSuccess, Toast.LENGTH_SHORT).show()
                        var resp = response!!.body()!!.DATA!!
                        Session.user.Ime=resp.Ime
                        Session.user.Prezime= resp.Prezime
                        Session.user.Email = resp.Email
                        Session.user.KorisnickoIme = resp.KorisnickoIme
                        Session.user.Id_Uloge = resp.Id_Uloge
                        Session.user.Naziv_Uloge = resp.Naziv_Uloge
                        Session.user.StanjeRacuna = resp.StanjeRacuna
                        Session.user.DozvolaPregledTransakcija = resp.DozvolaPregledTransakcija
                        Session.user.DozvolaUpravljanjeStanjemRacuna = resp.DozvolaUpravljanjeStanjemRacuna
                        Session.user.DozvolaUpravljanjeUlogama = resp.DozvolaUpravljanjeUlogama
                        Session.user.DozvolaUvidUStatistiku = resp.DozvolaUvidUStatistiku
                        Session.user.LoginTime = resp.LoginTime
                        Session.user.Token = resp.Token

                        showProducts()
                    }
                }
            })

        }

    private fun showProducts(){
        val intent = Intent(this, ShowProductsActivity::class.java)
        startActivity(intent)
    }
}
