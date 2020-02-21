package com.example.qr

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pop_sajamv2.Session
import com.example.webservice.Common.Common
import com.example.webservice.Model.OneInvoiceResponse
import kotlinx.android.synthetic.main.activity_qrcode.*
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigInteger
import android.animation.AnimatorListenerAdapter
import com.example.core.BaseActivity
import com.example.webservice.Model.Invoice


class QRCodeActivity : BaseActivity() {
    var deleteInvoice = true
    var cancelled=false
    var loop = true
    lateinit var menuIntent :Intent
    lateinit var detailsIntent:Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrcode)
        val total = intent.extras!!.get("Total").toString()
        menuIntent = intent.extras!!.get("menuIntent") as Intent
        detailsIntent = intent.extras!!.get("detailsIntent") as Intent
        val expandedTotal = expand(total)
        imageView.setImageBitmap(QRCode.generateQRCode(expandedTotal))
        animation_loading.addAnimatorListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                super.onAnimationStart(animation)
                animation_loading.setMinAndMaxFrame(0,15)
            }
        })
        showInvoice(total.toInt())
    }

    private fun showInvoice(id:Int){
        var api = Common.api
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
                            Toast.makeText(this@QRCodeActivity, t.message, Toast.LENGTH_SHORT)
                                .show()
                            loop=false
                        }

                        override fun onResponse(
                            call: Call<OneInvoiceResponse>,
                            response: Response<OneInvoiceResponse>
                        ) {
                            if (response.body()!!.DATA!!.Id == null) {
                                Toast.makeText(
                                    this@QRCodeActivity,
                                    "Transakcija poništena",
                                    Toast.LENGTH_SHORT
                                ).show()
                                var intent = menuIntent
                                    //Intent(this@QRCodeActivity, MainMenuSeller::class.java)
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
                    //Intent(this@QRCodeActivity, InvoiceDetailsActivity::class.java)
                intent.putExtra("invoice", data)
                startActivity(intent)
                finishAffinity()
            }
        })
    }

    private fun expand(total:String):String{
        val x = BigInteger(total)
        val y = x * Session.expander
        return y.toString()
    }

    override fun onStop() {
        super.onStop()
        if (deleteInvoice){
            if (!cancelled){
                var api = Common.api
                api.getOneInvoice(Session.user.Token, true,Session.user.KorisnickoIme, intent.extras!!.get("Total").toString()).enqueue(object: Callback<OneInvoiceResponse>{
                    override fun onFailure(call: Call<OneInvoiceResponse>, t: Throwable) {
                        Toast.makeText(this@QRCodeActivity, t.message, Toast.LENGTH_SHORT).show()
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
                                    Toast.makeText(this@QRCodeActivity, t2.message, Toast.LENGTH_SHORT).show()
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
