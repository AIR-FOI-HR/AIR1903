package com.example.pop

import android.app.PendingIntent
import android.content.ClipDescription
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity

class GetNfcMessageActivity : AppCompatActivity() {

    private var nfcAdapter: NfcAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_nfc_message)

        var nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        //check if NFC is supported
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        //Log.d("NFC supported", (nfcAdapter != null).toString())
        //Log.d("NFC enabled", (nfcAdapter?.isEnabled).toString())

        val isNfcSupported: Boolean = this.nfcAdapter != null
        //this.nfcAdapter = NfcAdapter.getDefaultAdapter(this)?.let { it }

        if (!isNfcSupported) {
            Log.d("NFC SUPPORTED_RCV", "=> FALSE")
        }else{
            Log.d("NFC SUPPORTED_RCV", "=> TRUE")
        }

        if (!nfcAdapter?.isEnabled!!) {
            Log.d("NFC ENABLED_RCV", "=> FALSE")
        }else{
            Log.d("NFC ENABLED_RCV", "=> TRUE")
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        receiveMessageFromDevice(intent)
    }

    override fun onResume() {
        super.onResume()
        enableForegroundDispatch(this, this.nfcAdapter)
        receiveMessageFromDevice(intent)
    }

    override fun onPause() {
        super.onPause()
        disableForegroundDispatch(this, this.nfcAdapter)
    }

    private fun receiveMessageFromDevice(intent: Intent) {
        val action = intent.action
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == action) {
            val parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            with(parcelables) {
                val inNdefMessage = this[0] as NdefMessage
                val inNdefRecords = inNdefMessage.records
                val ndefRecord_0 = inNdefRecords[0]

                val inMessage = String(ndefRecord_0.payload)
                //toast message
                val text = inMessage
                val duration = Toast.LENGTH_LONG

                val toast = Toast.makeText(applicationContext, inMessage, duration)
                toast.show()
            }
        }
    }

    private fun enableForegroundDispatch(activity: AppCompatActivity, adapter: NfcAdapter?) {
        val intent = Intent(activity.applicationContext, activity.javaClass)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP

        val pendingIntent = PendingIntent.getActivity(activity.applicationContext, 0, intent, 0)

        val filters = arrayOfNulls<IntentFilter>(1)
        val techList = arrayOf<Array<String>>()

        filters[0] = IntentFilter()
        with(filters[0]) {
            this?.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED)
            this?.addCategory(Intent.CATEGORY_DEFAULT)
            try {
                this?.addDataType(MIME_TEXT_PLAIN)
            } catch (ex: IntentFilter.MalformedMimeTypeException) {
                throw RuntimeException("Check your MIME type")
            }
        }

        adapter?.enableForegroundDispatch(activity, pendingIntent, filters, techList)
    }

    private fun disableForegroundDispatch(activity: AppCompatActivity, adapter: NfcAdapter?) {
        adapter?.disableForegroundDispatch(activity)
    }

}
