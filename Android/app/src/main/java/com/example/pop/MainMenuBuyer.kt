package com.example.pop

import android.app.Activity
import android.app.PendingIntent
import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pop_sajamv2.Session
import com.example.webservice.Common.Common
import com.example.webservice.Model.Invoice
import com.example.webservice.Model.OneInvoiceResponse
import com.example.webservice.Response.IMyAPI
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_main_menu_seller.*
import kotlinx.android.synthetic.main.dialog_payment_method.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigInteger


const val MIME_TEXT_PLAIN = "text/plain"

class MainMenuBuyer : AppCompatActivity() {

    lateinit var mService: IMyAPI
    lateinit var invoice:Invoice
    //NFC


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu_buyer)
        mService = Common.api

        username.text = Session.user.Ime + " " + Session.user.Prezime;


        /*var nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        Log.d("NFC supported", (nfcAdapter != null).toString())
        Log.d("NFC enabled", (nfcAdapter?.isEnabled).toString())

        val isNfcSupported: Boolean = this.nfcAdapter != null
        this.nfcAdapter = NfcAdapter.getDefaultAdapter(this)?.let { it }*/

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

        val dialogView = layoutInflater.run { inflate(R.layout.dialog_payment_method, null) }
        val dialogWindow = PopupWindow(
            dialogView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        card_sell_items.setOnClickListener {
            dialogWindow.showAtLocation(it, Gravity.CENTER, 0, 0)
            dialogWindow.dimBehind()
        }
        card_invoices.setOnClickListener { showInvoices() }
        card_wallet.setOnClickListener { showWalletBalance() }

        dialogView.btn_close_payment_dialog.setOnClickListener { dialogWindow.dismiss() }
        dialogView.btn_qr_code.setOnClickListener {
            val scanner = IntentIntegrator(this)
            scanner.initiateScan()
        }

        dialogView.btn_nfc.setOnClickListener {
            val adapter = NfcAdapter.getDefaultAdapter(this)
            adapter.enableReaderMode(this, this, NfcAdapter.FLAG_READER_NFC_A, null)

            //onNewIntent(intent)
        }
    }

    private fun showWalletBalance() {
        val intent = Intent(this, ShowWalletBalanceActivity::class.java)
        startActivity(intent)
    }

    private fun showInvoices() {
        val intent = Intent(this, ShowInvoicesActivity::class.java)
        startActivity(intent)
    }

    private fun PopupWindow.dimBehind() {
        val container = contentView.rootView
        val context = contentView.context
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val p = container.layoutParams as WindowManager.LayoutParams
        p.flags = p.flags or WindowManager.LayoutParams.FLAG_DIM_BEHIND
        p.dimAmount = 0.7f
        wm.updateViewLayout(container, p)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            if (result != null) {
                if (result.contents == null) {
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
                } else {
                    /*Toast.makeText(
                        this,
                        "Scanned: " + (BigInteger(result.contents) / Session.expander).toString(),
                        Toast.LENGTH_LONG
                    ).show()*/
                    var api = Common.api
                    api.finalizeInvoice(Session.user.Token, true, Session.user.KorisnickoIme, (BigInteger(result.contents) / Session.expander).toInt()).enqueue(object :Callback<OneInvoiceResponse>{
                        override fun onFailure(call: Call<OneInvoiceResponse>, t: Throwable) {
                            Toast.makeText(this@MainMenuBuyer, t.message, Toast.LENGTH_SHORT).show()
                        }

                        override fun onResponse(
                            call: Call<OneInvoiceResponse>,
                            response: Response<OneInvoiceResponse>
                        ) {
                            if (response.body()!!.STATUSMESSAGE=="INVOICE FINALIZED") {
                                val invoice = response.body()!!.DATA!! as Invoice
                                var intent =
                                    Intent(this@MainMenuBuyer, InvoiceDetailsActivity::class.java)
                                intent.putExtra("invoice", invoice)
                                startActivity(intent)
                                finishAffinity()
                            }
                            else if (response.body()!!.STATUSMESSAGE=="MISSING AMOUNT"){
                                Toast.makeText(this@MainMenuBuyer, "Nekog od proizvoda nema na skladištu", Toast.LENGTH_SHORT).show()
                            }
                            else if (response.body()!!.STATUSMESSAGE=="MISSING BALANCE"){
                                Toast.makeText(this@MainMenuBuyer, "Nemate dovoljno novaca na računu", Toast.LENGTH_SHORT).show()
                            }

                        }
                    })




                }
            } else {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
            0
        )
        //val adapter = NfcAdapter.getDefaultAdapter(this)
        //adapter.enableForegroundDispatch(this, pendingIntent, null, null)
    }

    override fun onPause() {
        super.onPause()
        //val adapter = NfcAdapter.getDefaultAdapter(this)
        //adapter.disableForegroundDispatch(this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val tag: Tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
        receiveMessageFromDevice(intent)
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
                //tvIncomingMessage?.text = inMessage


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
                this?.addDataType(MIMETYPE_TEXT_PLAIN)
            } catch (ex: IntentFilter.MalformedMimeTypeException) {
                throw RuntimeException("Check your MIME type")
            }
        }

        adapter?.enableForegroundDispatch(activity, pendingIntent, filters, techList)
    }

    private fun disableForegroundDispatch(activity: AppCompatActivity, adapter: NfcAdapter?) {
        adapter?.disableForegroundDispatch(activity)
    }



    private fun NfcAdapter.enableReaderMode(
        mainMenuBuyer: MainMenuBuyer,
        mainMenuBuyer1: MainMenuBuyer,
        flagReaderNfcA: Int,
        nothing: Nothing?
    ) {

    }
}