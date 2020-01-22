package com.example.pop

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.pop_sajamv2.Session
import com.example.qr.QRCode
import kotlinx.android.synthetic.main.activity_qrcode.*
import java.math.BigInteger

class QRCodeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrcode)
        val total = intent.getStringExtra("Total")
        val expandedTotal = expand(total)
        imageView.setImageBitmap(QRCode.generateQRCode(expandedTotal))
    }

    private fun expand(total:String):String{
        val x = BigInteger(total)
        val y = x * Session.expander
        return y.toString()
    }
}
