package com.example.pop

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.NfcManager
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.Toast
import com.example.android_code.CodePaymentActivity
import com.example.core.BaseActivity
import com.example.nfc.GetNfcMessageActivity
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

class MainMenuBuyer : BaseActivity() {

    lateinit var mService: IMyAPI
    var dialogWindow: PopupWindow? = null;
    //lateinit var invoice:Invoice
    //NFC


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu_buyer)
        mService = Common.api

        username.text = Session.user.Ime + " " + Session.user.Prezime;


        val dialogView = layoutInflater.run { inflate(R.layout.dialog_payment_method, null) }
        dialogWindow = PopupWindow(
            dialogView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        card_sell_items.setOnClickListener {
            dialogWindow!!.showAtLocation(it, Gravity.CENTER, 0, 0)
            dialogWindow!!.dimBehind()
        }

        dialogView.btn_close_payment_dialog.setOnClickListener { dialogWindow!!.dismiss() }
        dialogView.btn_qr_code.setOnClickListener {
            val scanner = IntentIntegrator(this)
            scanner.initiateScan()
        }

        dialogView.btn_code.setOnClickListener {
            val intentDetails = Intent(this, InvoiceDetailsActivity::class.java)
            val intent = Intent(this, CodePaymentActivity::class.java)
            intent.putExtra("detailsIntent", intentDetails)
            dialogWindow!!.dismiss()
            this.startActivity(intent)
        }


        card_invoices.setOnClickListener { showInvoices() }
        card_wallet.setOnClickListener { showWalletBalance() }

        dialogView.btn_nfc.setOnClickListener {
            val intentNFC = Intent(this, GetNfcMessageActivity::class.java)
            val intentDetails = Intent(this, InvoiceDetailsActivity::class.java)
            intentNFC.putExtra("detailsIntent", intentDetails)
            var nfcAdapter = NfcAdapter.getDefaultAdapter(this)
            nfcAdapter = NfcAdapter.getDefaultAdapter(this)
            val manager =
                this.getSystemService(Context.NFC_SERVICE) as NfcManager
            val adapter = manager.defaultAdapter
            if (adapter != null && adapter.isEnabled) {
                this.startActivity(intentNFC)
            }else{
                val toast = Toast.makeText(this, getString(R.string.toast_nfc_unavailable), Toast.LENGTH_LONG)
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

    override fun onBackPressed() {
        if(dialogWindow != null && dialogWindow!!.isShowing)
            dialogWindow!!.dismiss()
        else {
            val exitIntent = Intent(Intent.ACTION_MAIN)
            exitIntent.addCategory(Intent.CATEGORY_HOME)
            exitIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(exitIntent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val payment= QRPayment()

        if (resultCode == Activity.RESULT_OK) {
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            if (result != null) {
                if (result.contents == null) {
                    Toast.makeText(this, getString(R.string.toast_transaction_cancelled), Toast.LENGTH_LONG).show()
                } else {
                    payment.id = (BigInteger(result.contents) / Session.expander).toInt()

                    val intent =
                        Intent(this, InvoiceDetailsActivity::class.java)

                    val response = payment.pay(this)

                    lateinit var invoice:Invoice

                    when (response.STATUSMESSAGE) {
                        "INVOICE FINALIZED" -> {
                            invoice = response.DATA!! as Invoice
                            intent.putExtra("invoice", invoice)
                            startActivity(intent)
                            finishAffinity()
                        }
                        "MISSING AMOUNT" -> {
                            Toast.makeText(this, getString(com.example.core.R.string.toast_out_of_stock), Toast.LENGTH_SHORT).show()
                        }
                        "MISSING BALANCE" -> {
                            Toast.makeText(this, getString(com.example.core.R.string.toast_out_of_balance), Toast.LENGTH_SHORT).show()
                        }
                        "NO BUYING FROM OWN STORE" -> {
                            Toast.makeText(this, getString(com.example.core.R.string.toast_buy_from_own_store), Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            Toast.makeText(this, response.STATUSMESSAGE, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }
}
