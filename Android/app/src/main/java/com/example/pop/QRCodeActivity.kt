package com.example.pop

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pop_sajamv2.Session
import com.example.qr.QRCode
import com.example.webservice.Common.Common
import com.example.webservice.Model.InvoiceResponse
import com.example.webservice.Model.OneInvoiceResponse
import kotlinx.android.synthetic.main.activity_qrcode.*
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigInteger

class QRCodeActivity : AppCompatActivity() {
    var deleteInvoice = true
    var cancelled=false
    var loop = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrcode)
        val total = intent.getStringExtra("Total")
        val expandedTotal = expand(total)
        imageView.setImageBitmap(QRCode.generateQRCode(expandedTotal))
        //imageView.setOnClickListener { showInvoice(total.toInt()) }
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
                                var intent = Intent(this@QRCodeActivity, MainMenuSeller::class.java)
                                cancelled = true
                                loop = false
                                startActivity(intent)
                                finishAffinity()

                            } else if (response.body()!!.DATA!!.Kupac != null) {
                                var intent =
                                    Intent(this@QRCodeActivity, InvoiceDetailsActivity::class.java)
                                intent.putExtra("invoice", response.body()!!.DATA)
                                deleteInvoice = false
                                loop = false
                                startActivity(intent)
                                finishAffinity()
                            }
                        }
                    })
                delay(1000)
            }
        }



    }
    private fun expand(total:String):String{
        val x = BigInteger(total)
        val y = x * Session.expander
        return y.toString()
    }

    override fun onStop() {
        super.onStop()
        if (deleteInvoice==true){
            if (cancelled==false){
                var api = Common.api
                api.getOneInvoice(Session.user.Token, true,Session.user.KorisnickoIme, intent.getStringExtra("Total")).enqueue(object: Callback<OneInvoiceResponse>{
                    override fun onFailure(call: Call<OneInvoiceResponse>, t: Throwable) {
                        Toast.makeText(this@QRCodeActivity, t.message, Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(
                        call: Call<OneInvoiceResponse>,
                        response: Response<OneInvoiceResponse>
                    ) {
                        if (response.body()!!.DATA!!.Id!=null){
                            api.deleteInvoice(Session.user.Token, true, intent.getStringExtra("Total")).enqueue(object: Callback<OneInvoiceResponse>{
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
