package com.example.qr

import android.content.Context
import com.example.pop_sajamv2.Session
import com.example.webservice.Common.Common
import com.example.webservice.Model.Invoice
import com.example.webservice.Model.OneInvoiceResponse
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Response

class QRPayment : com.example.core.PaymentInterface {
    var id :Int = 0
    lateinit var invoice:Invoice

    override fun pay(context: Context):OneInvoiceResponse {
        var api = Common.api

        var call: Call<OneInvoiceResponse> = api.finalizeInvoice(Session.user.Token, true, Session.user.KorisnickoIme, id)

        lateinit var response:Response<OneInvoiceResponse>
        runBlocking {
            var crt = GlobalScope.async {

                response = call.execute()

            }
            println(crt.await())
        }
        return response.body()!!

        /*if (response.body()!!.STATUSMESSAGE=="INVOICE FINALIZED") {
            invoice = response.body()!!.DATA!! as Invoice
            return invoice
        }
        else if (response.body()!!.STATUSMESSAGE=="MISSING AMOUNT"){
            Toast.makeText(context, "Nekog od proizvoda nema na skladištu", Toast.LENGTH_SHORT).show()
        }
        else if (response.body()!!.STATUSMESSAGE=="MISSING BALANCE"){
            Toast.makeText(context, "Nemate dovoljno novaca na računu", Toast.LENGTH_SHORT).show()
        }*/


        /*api.finalizeInvoice(Session.user.Token, true, Session.user.KorisnickoIme, id).enqueue(object :
            Callback<OneInvoiceResponse> {
            override fun onFailure(call: Call<OneInvoiceResponse>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<OneInvoiceResponse>,
                response: Response<OneInvoiceResponse>
            ) {
                if (response.body()!!.STATUSMESSAGE=="INVOICE FINALIZED") {
                    invoice = response.body()!!.DATA!! as Invoice
                    return@pay invoice
                    /*intent.putExtra("invoice", invoice)
                    context.startActivity(intent)
                    finishAffinity(context as Activity)*/
                }
                else if (response.body()!!.STATUSMESSAGE=="MISSING AMOUNT"){
                    Toast.makeText(context, "Nekog od proizvoda nema na skladištu", Toast.LENGTH_SHORT).show()
                }
                else if (response.body()!!.STATUSMESSAGE=="MISSING BALANCE"){
                    Toast.makeText(context, "Nemate dovoljno novaca na računu", Toast.LENGTH_SHORT).show()
                }

            }
        })*/
    }
}