package com.example.pop

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nfc.NFCPayment
import com.example.pop_sajamv2.Session
import com.example.webservice.Common.Common
import com.example.webservice.Model.Invoice
import com.example.webservice.Model.OneInvoiceResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GetNfcMessageActivity : AppCompatActivity() {

    private var nfcAdapter: NfcAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_nfc_message)
        var nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        nfcAdapter.setNdefPushMessage(null, this)
        //check if NFC is supported
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        //Log.d("NFC supported", (nfcAdapter != null).toString())
        //Log.d("NFC enabled", (nfcAdapter?.isEnabled).toString())

        val isNfcSupported: Boolean = this.nfcAdapter != null
        //this.nfcAdapter = NfcAdapter.getDefaultAdapter(this)?.let { it }

        /*if (!isNfcSupported) {
            Log.d("NFC SUPPORTED_RCV", "=> FALSE")
        }else{
            Log.d("NFC SUPPORTED_RCV", "=> TRUE")
        }

        if (!nfcAdapter?.isEnabled!!) {
            Log.d("NFC ENABLED_RCV", "=> FALSE")
        }else{
            Log.d("NFC ENABLED_RCV", "=> TRUE")
        }*/
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        receiveMessageFromDevice(intent)
    }

    /*override fun onResume() {
        super.onResume()
        enableForegroundDispatch(this, this.nfcAdapter)
        receiveMessageFromDevice(intent)
    }

     */

    override fun onResume() {
        super.onResume()
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
            0
        )
        val adapter = NfcAdapter.getDefaultAdapter(this)
        adapter.enableForegroundDispatch(this, pendingIntent, null, null)
        receiveMessageFromDevice(intent)
    }

    /*override fun onPause() {
        super.onPause()
        disableForegroundDispatch(this, this.nfcAdapter)
    }

     */


    override fun onPause() {
        super.onPause()
        val adapter = NfcAdapter.getDefaultAdapter(this)
        adapter.disableForegroundDispatch(this)
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
                var intent =
                    Intent(this@GetNfcMessageActivity , InvoiceDetailsActivity::class.java)

                var payment = NFCPayment()
                payment.id=text.toInt()
                payment.pay(this@GetNfcMessageActivity,intent)

                //val toast = Toast.makeText(applicationContext, inMessage, duration)

                //toast.show()


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
