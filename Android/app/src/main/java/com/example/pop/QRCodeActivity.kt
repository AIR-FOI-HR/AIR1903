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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigInteger

class QRCodeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrcode)
        val total = intent.getStringExtra("Total")
        val expandedTotal = expand(total)
        imageView.setImageBitmap(QRCode.generateQRCode(expandedTotal))
        imageView.setOnClickListener { showInvoice(total.toInt()) }
    }

    private fun showInvoice(id:Int){
        var api = Common.api
        api.getOneInvoice(Session.user.Token,true, Session.user.KorisnickoIme, id.toString()).enqueue(object:Callback<OneInvoiceResponse>{
            override fun onFailure(call: Call<OneInvoiceResponse>, t: Throwable) {
                Toast.makeText(this@QRCodeActivity, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<OneInvoiceResponse>,
                response: Response<OneInvoiceResponse>
            ) {
                if (response.body()!!.DATA!!.Id == null){
                    Toast.makeText(this@QRCodeActivity, "Transakcija poništena", Toast.LENGTH_SHORT).show()
                    var intent = Intent(this@QRCodeActivity, MainMenuSeller::class.java)
                    startActivity(intent)
                    finishAffinity()
                }
                else if (response.body()!!.DATA!!.Kupac!=null) {
                    var intent = Intent(this@QRCodeActivity, InvoiceDetailsActivity::class.java)
                    intent.putExtra("invoice", response.body()!!.DATA)
                    startActivity(intent)
                    finishAffinity()
                }
            }
        })



    }
    private fun expand(total:String):String{
        val x = BigInteger(total)
        val y = x * Session.expander
        return y.toString()
    }
}
