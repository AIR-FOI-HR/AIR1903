package com.example.pop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TableLayout
import com.example.webservice.Common.Common
import com.example.webservice.Model.Product
import com.example.webservice.Response.IMyAPI
import kotlinx.android.synthetic.main.activity_main_menu.*
import com.example.pop_sajamv2.Session
class MainMenu : AppCompatActivity() {
    internal lateinit var mService: IMyAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
        mService = Common.api

        username.text = Session.user.Ime + " " + Session.user.Prezime;


        showAllProductsButton.setOnClickListener{showProducts()}
        addProductButton.setOnClickListener{addNewProduct()}
        editProductButton.setOnClickListener { editProduct() }
        showAllPackages.setOnClickListener { showPackages() }

        tabLayout.setOnClickListener{showLayout()}
    }
    private fun showProducts(){
        val intent = Intent(this, ShowProductsActivity::class.java)
        startActivity(intent)
    }

    private fun showLayout(){
        val intent = Intent(this, TabLayoutActivity::class.java)
        startActivity(intent)
    }

    private fun addNewProduct(){
        val intent = Intent(this, ManageProductsActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("previousActivity", 2)
        startActivity(intent)
    }

    private fun showPackages()  {
        val intent = Intent(this, ShowPackagesActivity::class.java)
        startActivity(intent)
    }

    private fun editProduct(){

        val intent = Intent(this, ManagePackagesActivity::class.java)
        startActivity(intent)
    }
}
