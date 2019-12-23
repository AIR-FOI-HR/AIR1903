package com.example.pop

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class ManagePackagesActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        setContentView(R.layout.activity_manage_packages)
    }

}