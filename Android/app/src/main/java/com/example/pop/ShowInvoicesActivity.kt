package com.example.pop

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.example.core.BaseActivity
import com.example.pop.adapters.InvoiceAdapter
import com.example.pop_sajamv2.Session
import com.example.webservice.Common.Common
import com.example.webservice.Model.Invoice
import com.example.webservice.Model.InvoiceResponse
import kotlinx.android.synthetic.main.activity_show_invoices.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ShowInvoicesActivity : BaseActivity() {
    private lateinit var invoiceAdapter :InvoiceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_invoices)

        invoiceAdapter = InvoiceAdapter()
        layoutShowReceiptsRecycler.adapter = invoiceAdapter
        getInvoices()
    }

    private fun getInvoices(){
        val api = Common.api
        api.getAllInvoices(Session.user.Token, true, Session.user.KorisnickoIme).enqueue(object: Callback<InvoiceResponse>{
            override fun onFailure(call: Call<InvoiceResponse>, t: Throwable) {
                Toast.makeText(this@ShowInvoicesActivity, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<InvoiceResponse>,
                response: Response<InvoiceResponse>
            ) {
                if (response.body()!!.STATUSMESSAGE=="USER NOT IN STORE"){
                    Toast.makeText(this@ShowInvoicesActivity, "Korisnik nije dio trgovine", Toast.LENGTH_LONG).show()
                    return
                }else if (response.body()?.STATUSMESSAGE=="SUCCESS, NO INVOICES"){
                    Toast.makeText(this@ShowInvoicesActivity, "Trgovina nema račune", Toast.LENGTH_LONG).show()
                    return
                }
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
                        invoiceAdapter.data=invoices
                    }
                    else -> {
                        Toast.makeText(
                            this@ShowInvoicesActivity,
                            response.body()!!.STATUSMESSAGE,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        })
    }
}
