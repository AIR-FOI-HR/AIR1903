package com.example.pop

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

var packageTouchX : Float = 0f

class ManagePackagesActivity : AppCompatActivity() {


    var packageId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_packages)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

}