package com.example.pop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.webservice.Common.Common
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


        showAllProductsButton.setOnClickListener{showItems()}
        btn_items.setOnClickListener{showItems()}
        showWalletBalanceButton.setOnClickListener{showWalletBalance()}
        //editProductButton.setOnClickListener { editProduct() }
    }

    private fun showItems(){
        val intent = Intent(this, ShowItemsActivity::class.java)
        startActivity(intent)
    }

    private fun addNewProduct(){
        val intent = Intent(this, ManageProductsActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("previousActivity", 2)
        startActivity(intent)
    }


    private fun editProduct(){

        val intent = Intent(this, ManagePackagesActivity::class.java)
        startActivity(intent)
    }

    private fun showWalletBalance(){
        val intent = Intent(this, ShowWalletBalanceActivity::class.java)
        startActivity(intent)
    }

    private fun showInvoices() {
        val intent = Intent(this, ShowReceiptsActivity::class.java)
        startActivity(intent)
    }
}
