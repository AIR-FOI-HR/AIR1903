package com.example.pop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pop.adapters.InvoiceAdapter

class ShowInvoicesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_invoices)

        val adapter = InvoiceAdapter()
        //adapter.data =
    }
}
