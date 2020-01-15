package com.example.pop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.example.pop.adapters.SellItemsAdapter
import com.example.qr.QRCode
import com.example.webservice.Model.Item
import com.example.webservice.Model.PackageClass
import com.example.webservice.Model.Product
import kotlinx.android.synthetic.main.activity_sell_items.*
import kotlinx.android.synthetic.main.sell_item_list.*
import java.lang.System.out

class SellItemsActivity : AppCompatActivity() {

    private var itemsList : List<Item> = listOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sell_items)
        layoutSellItemsButtonNFC.setOnClickListener { startNFC() }
        layoutSellItemsButtonQR.setOnClickListener { startQR() }

        itemsList = (intent.getSerializableExtra("items") as ItemsWrapper).getItems()

        val adapter = SellItemsAdapter()
        layoutSellItemsRecycler.adapter = adapter
        adapter.data = itemsList
    }

    private fun startNFC() {
        //Pokrece placanje sa nfc-om
    }

    private fun startQR() {
        val intent = Intent(this, QRCodeActivity::class.java)
        //layoutSellItemsTotalPrice  -> iznos u banneru iznad popisa selektiranih proizvoda
        intent.putExtra("Total", layoutSellItemListPrice.text)
        startActivity(intent)
    }
}
