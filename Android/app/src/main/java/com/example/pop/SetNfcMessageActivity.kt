package com.example.pop

import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nfcm.OutcomingNfcManager

class SetNfcMessageActivity : AppCompatActivity(), OutcomingNfcManager.INfcActivity {


    private var nfcAdapter: NfcAdapter? = null
    private lateinit var outcomingNfcCallback: OutcomingNfcManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_nfc_message)

        //NFC
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        val isNfcSupported: Boolean = this.nfcAdapter != null

        this.outcomingNfcCallback = OutcomingNfcManager(this)
        this.nfcAdapter?.setOnNdefPushCompleteCallback(outcomingNfcCallback, this)
        this.nfcAdapter?.setNdefPushMessageCallback(outcomingNfcCallback, this)


        val invoiceId = intent.getStringExtra("InvoiceID")

        val text : String = "HELLO"
        setOutGoingMessage()
    }

    override fun onNewIntent(intent: Intent) {
        this.intent = intent
        super.onNewIntent(intent)
    }

    val textToShow : String = "HELLO"
    private fun setOutGoingMessage() {
        val outMessage = textToShow
    }

    override fun getOutcomingMessage(): String = textToShow

    override fun signalResult() {
        runOnUiThread {
            Toast.makeText(this, "Payment Successful!", Toast.LENGTH_SHORT).show()
        }
    }

}
