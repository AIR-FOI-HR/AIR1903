package com.example.pop

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_manage_packages.*


class ManagePackagesActivity : AppCompatActivity() {
    private var activityStarted: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(!activityStarted){
            setContentView(R.layout.activity_manage_packages)
            activityStarted = true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}