package com.example.pop

import android.content.Context
import android.content.Intent
import android.nfc.NfcManager
import android.widget.Toast

class NFCPayment : PaymentInterface {
    override fun startPayment(context: Context, id: Int) {
        val manager =
            context.getSystemService(Context.NFC_SERVICE) as NfcManager
        val adapter = manager.defaultAdapter
        if (adapter != null && adapter.isEnabled) {
            val intent = Intent(context, SetNfcMessageActivity::class.java)
            intent.putExtra("InvoiceID", id.toString())
            context.startActivity(intent)
        }else{
            val text = "NFC disabled or unavailable!"
            val duration = Toast.LENGTH_LONG

            val toast = Toast.makeText(context, text, duration)
            toast.show()
        }
    }
}