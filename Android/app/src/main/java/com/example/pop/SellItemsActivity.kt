package com.example.pop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.example.webservice.Model.Item
import kotlinx.android.synthetic.main.activity_sell_items.*

class SellItemsActivity : AppCompatActivity() {

    var itemsList : List<Item> = listOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sell_items)
        layoutSellItemsAddButton.setOnClickListener { startItemsActivity() }
    }

    private fun startItemsActivity() {
        val intent = Intent(this, ShowItemsActivity::class.java)
        startActivity(intent)
    }
}
