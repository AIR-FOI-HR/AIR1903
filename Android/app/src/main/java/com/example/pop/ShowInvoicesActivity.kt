package com.example.pop

import android.content.Intent
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
import retrofit2.Callback
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.widget.Toast
import com.example.webservice.Model.InvoiceResponse
import retrofit2.Call
import retrofit2.Response
import kotlin.collections.ArrayList


class ShowInvoicesActivity : AppCompatActivity() {
    private lateinit var invoiceAdapter :InvoiceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_invoices)

        invoiceAdapter = InvoiceAdapter()
        layoutShowReceiptsRecycler.adapter = invoiceAdapter
        getInvoices()
        //adapter.data = getInvoices()
    }

    private fun getInvoices(){
        val api = Common.api
        api.getAllInvoices(Session.user.Token, true, Session.user.KorisnickoIme).enqueue(object: Callback<InvoiceResponse>{
            override fun onFailure(call: Call<InvoiceResponse>, t: Throwable) {
                println("DEBUG33-Neuspješno")
                println("DEBUG33-"+t.message)
                Toast.makeText(this@ShowInvoicesActivity, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<InvoiceResponse>,
                response: Response<InvoiceResponse>
            ) {
                var invoices = response.body()!!.DATA as ArrayList<Invoice>
                when {
                    response.body()!!.STATUSMESSAGE == "OLD TOKEN" -> {
                        val intent = Intent(this@ShowInvoicesActivity, LoginActivity::class.java)
                        Toast.makeText(
                            this@ShowInvoicesActivity,
                            "Sesija istekla, molimo prijavite se ponovno",
                            Toast.LENGTH_LONG
                        ).show()
                        Session.reset()
                        startActivity(intent)
                        finishAffinity()
                    }
                    response.body()!!.STATUSMESSAGE == "SUCCESS" -> {
                        println("DEBUG33-Uspješno primljeno")
                        invoiceAdapter.data=invoices
                    }
                    else -> {
                        Toast.makeText(
                            this@ShowInvoicesActivity,
                            response.body()!!.STATUSMESSAGE,
                            Toast.LENGTH_LONG
                        ).show()
                        println("DEBUG33-ELSE")
                    }
                }
                //if (packages != null) itemAdapter.submitList(packages!!)
            }
        })


    }
}
