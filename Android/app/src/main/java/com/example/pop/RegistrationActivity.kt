package com.example.pop

import android.content.Intent
import android.os.Bundle
import com.example.core.BaseActivity
import kotlinx.android.synthetic.main.activity_registration.*

const val SWIPE_THRESHOLD = 150
var touchX : Float = 0f

class RegistrationActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        layoutRegistrationTextLogin.setOnClickListener {
            startLoginActivity()
        }
    }

    fun startLoginActivity() {
        RegistrationData.Reset()
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        this.finish()
    }
}
