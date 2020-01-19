package com.example.pop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.webservice.Common.Common
import com.example.webservice.Response.IMyAPI
import kotlinx.android.synthetic.main.activity_main_menu_seller.*
import com.example.pop_sajamv2.Session

class MainMenuSeller : AppCompatActivity() {
    lateinit var mService: IMyAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu_seller)
        mService = Common.api

        username.text = Session.user.Ime + " " + Session.user.Prezime;


        card_sell_items.setOnClickListener{showItems()}
        card_invoices.setOnClickListener{showInvoices()}
        card_wallet.setOnClickListener{showWalletBalance()}
    }

    private fun showItems(){
        val intent = Intent(this, ShowItemsActivity::class.java)
        startActivity(intent)
    }

    private fun showWalletBalance(){
        val intent = Intent(this, ShowWalletBalanceActivity::class.java)
        startActivity(intent)
    }

    private fun showInvoices() {
        val intent = Intent(this, ShowInvoicesActivity::class.java)
        startActivity(intent)
    }
}
