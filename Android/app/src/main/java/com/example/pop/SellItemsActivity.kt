package com.example.pop

import android.content.Context
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.tech.NfcA
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.Toast
import com.example.nfcm.OutcomingNfcManager
import com.example.pop.adapters.SellItemsAdapter
import com.example.webservice.Model.Item
import kotlinx.android.synthetic.main.activity_sell_items.*
import kotlinx.android.synthetic.main.dialog_payment_method.view.*

class SellItemsActivity : AppCompatActivity(), OutcomingNfcManager.INfcActivity {

    private var itemsList : List<Item> = listOf()
    var totalValue: Double = 0.0

    //NFC
    private var nfcAdapter: NfcAdapter? = null
    private lateinit var outcomingNfcCallback: OutcomingNfcManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sell_items)

        itemsList = (intent.getSerializableExtra("items") as ItemsWrapper).getItems()

        val adapter = SellItemsAdapter()
        layoutSellItemsRecycler.adapter = adapter
        adapter.data = itemsList

        //NFC
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        val isNfcSupported: Boolean = this.nfcAdapter != null
        if (!isNfcSupported) {
            Log.d("NFC SUPPORTED_SND", "=> FALSE")
        }else{
            Log.d("NFC SUPPORTED_SND", "=> TRUE")
        }

        if (!nfcAdapter?.isEnabled!!) {
            Log.d("NFC ENABLED_SND", "=> FALSE")
        }else{
            Log.d("NFC ENABLED_SND", "=> TRUE")
        }


        this.outcomingNfcCallback = OutcomingNfcManager(this)
        this.nfcAdapter?.setOnNdefPushCompleteCallback(outcomingNfcCallback, this)
        this.nfcAdapter?.setNdefPushMessageCallback(outcomingNfcCallback, this)

        val dialogView = layoutInflater.run { inflate(R.layout.dialog_payment_method, null) }
        val dialogWindow = PopupWindow(
            dialogView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        btn_choose_payment_option.setOnClickListener{
            dialogWindow.showAtLocation(linearLayout, Gravity.CENTER, 0, 0)
            dialogWindow.dimBehind()
        }

        dialogView.btn_close_payment_dialog.setOnClickListener { dialogWindow.dismiss() }

        dialogView.btn_qr_code.setOnClickListener{
            startQR()
        }

        dialogView.btn_nfc.setOnClickListener{
            //provjeri je li dostupan NFC
            //ako je, ->
            setOutGoingMessage()
            //ako nije, toast message
        }
    }


    private fun startNFC() {
        //Pokrece placanje sa nfc-om
    }

    private fun startQR() {
        val intent = Intent(this, QRCodeActivity::class.java)
        intent.putExtra("Total", invoice_total_value.text)
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


    override fun onNewIntent(intent: Intent) {
        this.intent = intent
        super.onNewIntent(intent)
    }

    private fun setOutGoingMessage() {
        val outMessage = invoice_total_value.text.toString()
        this.invoice_total_value.text = outMessage
    }

    override fun getOutcomingMessage(): String = invoice_total_value.text.toString()

    override fun signalResult() {
        runOnUiThread {
            Toast.makeText(this, "Payment Successful!", Toast.LENGTH_SHORT).show()
        }
    }
}
