package com.example.qr

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.ActivityCompat.finishAffinity
import com.example.pop_sajamv2.Session
import com.example.webservice.Common.Common
import com.example.webservice.Model.Invoice
import com.example.webservice.Model.OneInvoiceResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QRPayment : com.example.core.PaymentInterface {
    var id :Int = 0


    override fun createInvoice(context: Context, id: Int, intent: Intent) {

        intent.putExtra("Total", id.toString())
        context.startActivity(intent)
    }

    override fun pay(context: Context, intent: Intent) {
        var api = Common.api
        api.finalizeInvoice(Session.user.Token, true, Session.user.KorisnickoIme, id).enqueue(object :
            Callback<OneInvoiceResponse> {
            override fun onFailure(call: Call<OneInvoiceResponse>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<OneInvoiceResponse>,
                response: Response<OneInvoiceResponse>
            ) {
                if (response.body()!!.STATUSMESSAGE=="INVOICE FINALIZED") {
                    val invoice = response.body()!!.DATA!! as Invoice

                    intent.putExtra("invoice", invoice)
                    context.startActivity(intent)
                    finishAffinity(context as Activity)
                }
                else if (response.body()!!.STATUSMESSAGE=="MISSING AMOUNT"){
                    Toast.makeText(context, "Nekog od proizvoda nema na skladištu", Toast.LENGTH_SHORT).show()
                }
                else if (response.body()!!.STATUSMESSAGE=="MISSING BALANCE"){
                    Toast.makeText(context, "Nemate dovoljno novaca na računu", Toast.LENGTH_SHORT).show()
                }

            }
        })
    }
}