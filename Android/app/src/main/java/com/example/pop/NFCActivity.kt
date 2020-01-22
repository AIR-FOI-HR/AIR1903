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

class NFCActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nfc)
    }

}
