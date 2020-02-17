package com.example.pop

import android.content.Intent
import android.os.Bundle
import com.example.core.BaseActivity

var packageTouchX : Float = 0f

class ManagePackagesActivity : BaseActivity() {


    var packageId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_packages)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

}