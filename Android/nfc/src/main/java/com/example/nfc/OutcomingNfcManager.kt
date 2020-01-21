package com.example.nfcm

import android.content.ClipDescription
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NdefRecord.createMime
import android.nfc.NfcAdapter
import android.nfc.NfcEvent
import android.os.Build
import androidx.annotation.RequiresApi

class OutcomingNfcManager(private val nfcActivity: INfcActivity) : NfcAdapter.CreateNdefMessageCallback, NfcAdapter.OnNdefPushCompleteCallback {

    //Potreban najmanje Jelly Bean OS
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun createNdefMessage(event: NfcEvent?): NdefMessage {
        // creating outcoming NFC message with a helper method
        // you could as well create it manually and will surely need, if Android version is too low
        val outString = nfcActivity.getOutcomingMessage()

        with(outString) {
            val outBytes = this.toByteArray()
            val outRecord = createMime(ClipDescription.MIMETYPE_TEXT_PLAIN, outBytes)
            return NdefMessage(outRecord)
        }
    }

    override fun onNdefPushComplete(event: NfcEvent?) {
        // onNdefPushComplete() is called on the Binder thread, so remember to explicitly notify
        // your view on the UI thread
        nfcActivity.signalResult()
    }

    interface INfcActivity {
        fun getOutcomingMessage(): String

        fun signalResult()
    }
}