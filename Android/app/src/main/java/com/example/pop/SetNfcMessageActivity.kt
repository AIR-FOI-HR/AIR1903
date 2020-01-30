package com.example.pop

import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nfcm.OutcomingNfcManager
import com.example.pop_sajamv2.Session
import com.example.webservice.Common.Common
import com.example.webservice.Model.OneInvoiceResponse
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SetNfcMessageActivity : AppCompatActivity(), OutcomingNfcManager.INfcActivity {

    var deleteInvoice = true
    var cancelled=false
    var loop = true
    private var nfcAdapter: NfcAdapter? = null
    lateinit var invoiceId:String
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


        invoiceId = intent.getStringExtra("InvoiceID")!!

        setOutGoingMessage()
    }

    override fun onNewIntent(intent: Intent) {
        this.intent = intent
        super.onNewIntent(intent)
    }

    //val textToShow : String = invoiceId
    private fun setOutGoingMessage() {
        val outMessage = invoiceId
        var api = Common.api
        val coroutine = GlobalScope.launch {
            while (loop) {
                api.getOneInvoice(
                    Session.user.Token,
                    true,
                    Session.user.KorisnickoIme,
                    invoiceId.toString()
                )
                    .enqueue(object : Callback<OneInvoiceResponse> {
                        override fun onFailure(call: Call<OneInvoiceResponse>, t: Throwable) {
                            Toast.makeText(this@SetNfcMessageActivity , t.message, Toast.LENGTH_SHORT)
                                .show()
                            loop=false
                        }

                        override fun onResponse(
                            call: Call<OneInvoiceResponse>,
                            response: Response<OneInvoiceResponse>
                        ) {
                            if (response.body()!!.DATA!!.Id == null) {
                                Toast.makeText(
                                    this@SetNfcMessageActivity ,
                                    "Transakcija poništena",
                                    Toast.LENGTH_SHORT
                                ).show()
                                var intent = Intent(this@SetNfcMessageActivity , MainMenuSeller::class.java)
                                cancelled = true
                                loop = false
                                startActivity(intent)
                                finishAffinity()

                            } else if (response.body()!!.DATA!!.Kupac != null) {
                                deleteInvoice = false
                                loop = false
                                val intent =
                                    Intent(this@SetNfcMessageActivity , InvoiceDetailsActivity::class.java)
                                intent.putExtra("invoice", response.body()!!.DATA)
                                startActivity(intent)
                                finishAffinity()
                            }
                        }
                    })
                delay(1000)
            }
        }
    }

    override fun getOutcomingMessage(): String = invoiceId

    override fun signalResult() {
        runOnUiThread {
            Toast.makeText(this, "Payment Successful!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStop() {
        super.onStop()
        if (deleteInvoice){
            if (!cancelled){
                var api = Common.api
                api.getOneInvoice(Session.user.Token, true,Session.user.KorisnickoIme, intent.getStringExtra("InvoiceID")).enqueue(object: Callback<OneInvoiceResponse>{
                    override fun onFailure(call: Call<OneInvoiceResponse>, t: Throwable) {
                        Toast.makeText(this@SetNfcMessageActivity, t.message, Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(
                        call: Call<OneInvoiceResponse>,
                        response: Response<OneInvoiceResponse>
                    ) {
                        if (response.body()!!.DATA!!.Id!=null){
                            api.deleteInvoice(Session.user.Token, true, intent.getStringExtra("InvoiceID")).enqueue(object: Callback<OneInvoiceResponse>{
                                override fun onFailure(
                                    call2: Call<OneInvoiceResponse>,
                                    t2: Throwable
                                ) {
                                    Toast.makeText(this@SetNfcMessageActivity, t2.message, Toast.LENGTH_SHORT).show()
                                }

                                override fun onResponse(
                                    call2: Call<OneInvoiceResponse>,
                                    response2: Response<OneInvoiceResponse>
                                ) {
                                }
                            })
                        }
                    }
                })
            }
        }

    }


}
