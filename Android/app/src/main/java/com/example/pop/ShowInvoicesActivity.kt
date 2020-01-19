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
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T



class ShowInvoicesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_invoices)

        val adapter = InvoiceAdapter()
        layoutShowReceiptsRecycler.adapter = adapter
        //adapter.data = getInvoices()
    }

    private fun getInvoices(){
        val api = Common.api


        //Prvi nacin - da procitamo godinu, mjesec, dan pa spojimo u string i ispisemo
        val day = Calendar.DAY_OF_MONTH
        val year = Calendar.YEAR
        val month = Calendar.MONTH

       //Drugi nacin - malo kompliciraniji, ali pravilniji
        val parser = SimpleDateFormat.getDateInstance()
        val formatter = SimpleDateFormat.getDateInstance()
        //val output = formatter.format(parser.parse("2018-12-14")!!)

        //Treci nacin da napravimo da se iz PHP dohvati string i ispise date

      /*val invoicesList : List<Invoice> = listOf(
            Invoice(1,"Foi", "10 OCT 2020", 10.0, 2, 1),
            Invoice(2,"Foi", "15 OCT 2020", 15.0, 2, 2))
        return invoicesList*/
    }
}
