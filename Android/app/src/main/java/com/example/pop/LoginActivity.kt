package com.example.pop

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.example.core.BaseActivity
import com.example.pop_sajamv2.Session
import com.example.webservice.Common.Common
import com.example.webservice.Model.ApiResponseUser
import com.example.webservice.Response.IMyAPI
import kotlinx.android.synthetic.main.activity_login4.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : BaseActivity() {
    private lateinit var mService:IMyAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login4)
        mService = Common.api

        val keyboard = HideKeyboard()
        loginLayout.setOnClickListener{keyboard.hideKeyboard(this)}
        input_login_username.setOnClickListener{input_login_username.setCursorVisible(true)}

        btn_login.setOnClickListener{authenticateUser(input_login_username.text.toString(),input_login_password.text.toString())}
        layoutLoginButtonRegister.setOnClickListener{startActivity(Intent(this@LoginActivity,RegistrationActivity::class.java))}
    }
        private fun authenticateUser(KorisnickoIme: String, Lozinka: String) {
            mService.storeUser(KorisnickoIme, Lozinka).enqueue(object : Callback<ApiResponseUser> {
                override fun onFailure(call: Call<ApiResponseUser>, t: Throwable) {
                    Toast.makeText(this@LoginActivity, t.message, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<ApiResponseUser>, response: Response<ApiResponseUser>) {
                    if (!response.body()!!.STATUS) {
                        if (response.body()!!.STATUSMESSAGE == "USER NEEDS STORE") {

                        Toast.makeText(this@LoginActivity, getString(R.string.toast_user_has_no_store),Toast.LENGTH_SHORT).show()
                            var intent = Intent(this@LoginActivity, RegistrationActivity::class.java)
                            RegistrationData.KorisnickoIme = response.body()!!.DATA!!.KorisnickoIme
                            Session.user.Token = response.body()!!.DATA!!.Token
                            intent.putExtra("Fragment",3)
                            startActivity(intent)
                            finishAffinity()
                        }
                        else
                            Toast.makeText(this@LoginActivity, response.body()!!.STATUSMESSAGE, Toast.LENGTH_SHORT).show()

                    }
                    else {
                        Toast.makeText(this@LoginActivity, R.string.toastLoginSuccess, Toast.LENGTH_SHORT).show()
                        val resp = response.body()!!.DATA!!
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
                        Session.user.Jezik = resp.Jezik

                        showMainMenu()
                    }
                }
            })

        }

    private fun showMainMenu(){
        application
        val intent = if(Session.user.Id_Uloge == 1)
                                Intent(this, MainMenuBuyer::class.java)
                            else
                                Intent(this, MainMenuSeller::class.java)
        startActivity(intent)
        finishAffinity()
    }
}
