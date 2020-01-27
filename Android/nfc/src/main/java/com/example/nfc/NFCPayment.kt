package com.example.pop

import android.content.Context
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.NfcManager
import android.util.Log
import android.widget.Toast

class NFCPayment : com.example.core.PaymentInterface {
    override fun createInvoice(context: Context, id: Int) {
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

    override fun pay(context: Context) {
        var nfcAdapter = NfcAdapter.getDefaultAdapter(context)
        nfcAdapter = NfcAdapter.getDefaultAdapter(context)
        Log.d("NFC supported", (nfcAdapter != null).toString())
        Log.d("NFC enabled", (nfcAdapter?.isEnabled).toString())
        val manager =
            context.getSystemService(Context.NFC_SERVICE) as NfcManager
        val adapter = manager.defaultAdapter
        if (adapter != null && adapter.isEnabled) {
            val intent = Intent(context, GetNfcMessageActivity::class.java)
            context.startActivity(intent)
        }else{
            val text = "NFC disabled or unavailable!"
            val duration = Toast.LENGTH_LONG

            val toast = Toast.makeText(context, text, duration)
            toast.show()
        }
    }

}