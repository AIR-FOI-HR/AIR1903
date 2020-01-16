package com.example.pop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pop.adapters.InvoiceAdapter
import com.example.pop_sajamv2.Session
import com.example.webservice.Common.Common
import com.example.webservice.Model.Invoice
import kotlinx.android.synthetic.main.activity_show_invoices.*
import javax.security.auth.callback.Callback

class ShowInvoicesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_invoices)

        val adapter = InvoiceAdapter()
        layoutShowReceiptsRecycler.adapter = adapter
        //adapter.data = getInvoices()
    }

    //private fun getInvoices() : List<Invoice> {
    //    val api = Common.api
    //    //Dohvatiti listu racuna u invoicesList
//
    //    //DEBUG
    //    var invoicesList : List<Invoice> = listOf(
    //        Invoice(1,"1.1.2020.", 20f),
    //        Invoice(2,"2.2.2222.", 100f),
    //        Invoice(61,"sutra.", 524.50f),
    //        Invoice(5523,"15.8.2001.", 100000.0f),
    //        Invoice(12134,"21.5.1995.", 22.6f) )
//
    //    return invoicesList
    //}
}
