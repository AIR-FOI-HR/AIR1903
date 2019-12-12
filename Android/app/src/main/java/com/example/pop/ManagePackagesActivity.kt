package com.example.pop

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.example.webservice.Model.Product
import kotlinx.android.synthetic.main.activity_manage_packages.*


class ManagePackagesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_packages)

        if(intent.hasExtra("package")) {
            if(intent.getSerializableExtra("package") != null) {
                
            }
        }

        layoutManagePackagesButtonSubmit.setOnClickListener { submitPackage() }
    }

    private fun submitPackage() {
        val intent = Intent(applicationContext, ShowPackagesActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        applicationContext.startActivity(intent)
    }
}