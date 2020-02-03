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
    }
}