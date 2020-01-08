package com.example.pop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pop.adapters.ReceiptAdapter

class ShowReceiptsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_receipts)

        val adapter = ReceiptAdapter()
        //adapter.data =
    }
}
