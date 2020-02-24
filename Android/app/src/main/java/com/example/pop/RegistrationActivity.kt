package com.example.pop

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import com.example.core.BaseActivity
import com.example.pop_sajamv2.Session
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.activity_tab_layout.*
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.example.pop.fragments.RegistrationStep3Fragment


const val SWIPE_THRESHOLD = 150
var touchX : Float = 0f

class RegistrationActivity : BaseActivity() {
    var currentFragment = 0

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

    override fun onBackPressed() {
        super.onBackPressed()
        if (currentFragment==5 || currentFragment==3) {
            var intent: Intent
            intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }
    }
}
