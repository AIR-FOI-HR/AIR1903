package com.example.android_code

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.example.pop_sajamv2.Session
import com.example.webservice.Common.Common
import com.example.webservice.Model.OneInvoiceResponse
import kotlinx.android.synthetic.main.activity_code.*
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.animation.AnimatorListenerAdapter
import android.util.Log
import com.example.core.BaseActivity
import com.example.webservice.Model.Invoice


class CodeActivity : BaseActivity() {
    var deleteInvoice = true
    var cancelled=false
    var loop = true
    lateinit var menuIntent: Intent
    lateinit var detailsIntent:Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       setContentView(R.layout.activity_code)
        val total = intent.extras!!.get("Total").toString()
        menuIntent = intent.extras!!.get("menuIntent") as Intent
        detailsIntent = intent.extras!!.get("detailsIntent") as Intent
        val invoiceCode = intent.extras!!.get("code").toString()
        input_invoice_code.text = invoiceCode

        animation_loading.addAnimatorListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                super.onAnimationStart(animation)
                animation_loading.setMinAndMaxFrame(0,15)
            }
        })
        showInvoice(total.toInt())
    }

    override fun onBackPressed() {
        deleteInvoice = true
        val intent = menuIntent
        startActivity(intent)
        finishAffinity()
    }

    private fun showInvoice(id:Int){
        val api = Common.api
        val asdf = GlobalScope.launch {
            while (loop) {
                api.getOneInvoice(
                    Session.user.Token,
                    true,
                    Session.user.KorisnickoIme,
                    id.toString()
                )
                    .enqueue(object : Callback<OneInvoiceResponse> {
                        override fun onFailure(call: Call<OneInvoiceResponse>, t: Throwable) {
                            Toast.makeText(this@CodeActivity, t.message, Toast.LENGTH_SHORT).show()
                            loop=false
                        }

                        override fun onResponse(
                            call: Call<OneInvoiceResponse>,
                            response: Response<OneInvoiceResponse>
                        ) {
                            if (response.body()!!.DATA!!.Id == null) {
                                Toast.makeText(this@CodeActivity, getString(R.string.toast_transaction_cancelled), Toast.LENGTH_SHORT).show()
                                val intent = menuIntent
                                cancelled = true
                                loop = false
                                startActivity(intent)
                                finishAffinity()

                            } else if (response.body()!!.DATA!!.Kupac != null) {
                                finishPayment(response.body()!!.DATA)
                            }
                        }
                    })
                delay(1000)
            }
        }

    }

    private fun finishPayment(data: Invoice?) {
        deleteInvoice = false
        loop = false
        animation_loading.addAnimatorListener(object : AnimatorListenerAdapter() {
            override fun onAnimationRepeat(animation: Animator?) {
                super.onAnimationRepeat(animation)
                animation_loading.setMaxFrame(60)
                animation_loading.repeatCount = 1
            }
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                val intent = detailsIntent
                intent.putExtra("invoice", data)
                startActivity(intent)
                finishAffinity()
            }
        })
    }

    override fun onStop() {
        super.onStop()
        if (deleteInvoice){
            Log.e("Racun", "Izbrisan");
            if (!cancelled){
                val api = Common.api
                api.getOneInvoice(Session.user.Token, true,Session.user.KorisnickoIme, intent.extras!!.get("Total").toString()).enqueue(object: Callback<OneInvoiceResponse>{
                    override fun onFailure(call: Call<OneInvoiceResponse>, t: Throwable) {
                        Toast.makeText(this@CodeActivity, t.message, Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(
                        call: Call<OneInvoiceResponse>,
                        response: Response<OneInvoiceResponse>
                    ) {
                        if (response.body()!!.DATA!!.Id!=null){
                            api.deleteInvoice(Session.user.Token, Session.user.KorisnickoIme, true, intent.extras!!.get("Total").toString()).enqueue(object: Callback<OneInvoiceResponse>{
                                override fun onFailure(
                                    call2: Call<OneInvoiceResponse>,
                                    t2: Throwable
                                ) {
                                    Toast.makeText(this@CodeActivity, t2.message, Toast.LENGTH_SHORT).show()
                                }

                                override fun onResponse(
                                    call2: Call<OneInvoiceResponse>,
                                    response2: Response<OneInvoiceResponse>
                                ) {
                                }
                            })
                        }
                    }
                })
            }
        }

    }
}
