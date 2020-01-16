package com.example.pop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pop.adapters.InvoiceAdapter
import com.example.pop_sajamv2.Session
import com.example.webservice.Common.Common
import com.example.webservice.Model.Invoice
import kotlinx.android.synthetic.main.activity_show_invoices.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Month
import java.time.format.DateTimeFormatter
import java.util.*
import javax.security.auth.callback.Callback

class ShowInvoicesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_invoices)

        val adapter = InvoiceAdapter()
        layoutShowReceiptsRecycler.adapter = adapter
        adapter.data = getInvoices()
    }

    private fun getInvoices() : List<Invoice> {
        val api = Common.api

        //DEBUG
        val date = Calendar.getInstance().time
        val formatter = SimpleDateFormat.getDateTimeInstance() //or use getDateInstance()
        val formatedDate = formatter.format(date)

        val invoicesList : List<Invoice> = listOf(
            Invoice(1,"Foi", date, 10.0, 2, 1),
            Invoice(2,"Foi", date, 15.0, 2, 2))
        return invoicesList
    }
}
