package com.example.pop

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.NfcManager
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nfc.NFCPayment
import com.example.pop_sajamv2.Session
import com.example.qr.QRPayment
import com.example.webservice.Common.Common
import com.example.webservice.Model.Invoice
import com.example.webservice.Response.IMyAPI
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_main_menu_seller.*
import kotlinx.android.synthetic.main.dialog_payment_method.view.*
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



        /*
        val isNfcSupported: Boolean = this.nfcAdapter != null
        this.nfcAdapter = NfcAdapter.getDefaultAdapter(this)?.let { it }
        */
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

        dialogView.btn_close_payment_dialog.setOnClickListener { dialogWindow.dismiss() }

        dialogView.btn_qr_code.setOnClickListener {
            val scanner = IntentIntegrator(this)
            scanner.initiateScan()
        }


        card_invoices.setOnClickListener { showInvoices() }
        card_wallet.setOnClickListener { showWalletBalance() }

        dialogView.btn_nfc.setOnClickListener {

            val intent = Intent(this, GetNfcMessageActivity::class.java)
            var nfcAdapter = NfcAdapter.getDefaultAdapter(this)
            nfcAdapter = NfcAdapter.getDefaultAdapter(this)
            Log.d("NFC supported", (nfcAdapter != null).toString())
            Log.d("NFC enabled", (nfcAdapter?.isEnabled).toString())
            val manager =
                this.getSystemService(Context.NFC_SERVICE) as NfcManager
            val adapter = manager.defaultAdapter
            if (adapter != null && adapter.isEnabled) {
                this.startActivity(intent)
            }else{
                val text = "NFC disabled or unavailable!"
                val duration = Toast.LENGTH_LONG

                val toast = Toast.makeText(this, text, duration)
                toast.show()
            }
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
        var payment= QRPayment()

        if (resultCode == Activity.RESULT_OK) {
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            if (result != null) {
                if (result.contents == null) {
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
                } else {
                    payment.id = (BigInteger(result.contents) / Session.expander).toInt()
                    var intent =
                        Intent(this, InvoiceDetailsActivity::class.java)
                    payment.pay(this, intent)
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }
}
