package com.example.pop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    internal lateinit var mService:IMyAPI


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mService = Common.api

        layoutLoginButtonRegister.setOnClickListener{startActivity(Intent(this@MainActivity,Registraacija::class.java))}
        layoutLoginButtonLogin.setOnClickListener{authenticateUser(layoutLoginInputUsername.text.toString(),layoutLoginInputPassword.text.toString())}
    }
}
