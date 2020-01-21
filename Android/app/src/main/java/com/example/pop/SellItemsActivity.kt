package com.example.pop

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pop.adapters.SellItemsAdapter
import com.example.pop_sajamv2.Session
import com.example.webservice.Common.Common
import com.example.webservice.Model.Item
import com.example.webservice.Model.OneInvoiceResponse
import kotlinx.android.synthetic.main.activity_sell_items.*
import kotlinx.android.synthetic.main.dialog_payment_method.view.*
import kotlinx.android.synthetic.main.sell_item_list.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigDecimal

class SellItemsActivity : AppCompatActivity() {

    private var itemsList : List<Item> = listOf()
    val adapter = SellItemsAdapter()
    var totalValue: BigDecimal = BigDecimal(0.0)
    var idRacuna:Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sell_items)

        itemsList = (intent.getSerializableExtra("items") as ItemsWrapper).getItems()


        layoutSellItemsRecycler.adapter = adapter
        adapter.data = itemsList



        btn_choose_payment_option.setOnClickListener{
            getInvoiceId()
        }


    }


    private fun startNFC() {
        //Pokrece placanje sa nfc-om
    }

    private fun startQR(id:Int) {
        val intent = Intent(this, QRCodeActivity::class.java)
        intent.putExtra("Total", id.toString())
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

    private fun getInvoiceId(){
        var api = Common.api
        var ids = adapter.getIds()
        var quantities = ArrayList<String>()
        var k = layoutSellItemsRecycler.findViewHolderForAdapterPosition(0)
        for (i in 0..adapter.itemCount-1){
            var k = layoutSellItemsRecycler.findViewHolderForAdapterPosition(i)
            quantities.add(k!!.itemView.layoutSellItemListQuantity.text.toString())

        }

        //TODO: Dok se na layout doda polje za popust, ubaciti ovdje umjesto 15.toString()
        api.generateInvoice(Session.user.Token,true,Session.user.KorisnickoIme, 15.toString(), ids, quantities).enqueue(object: Callback<OneInvoiceResponse>{
            override fun onFailure(call: Call<OneInvoiceResponse>, t: Throwable) {
                Toast.makeText(this@SellItemsActivity, t.message, Toast.LENGTH_SHORT).show()
            }
            override fun onResponse(call: Call<OneInvoiceResponse>, response: Response<OneInvoiceResponse>){
                var resp = response.body()!!.DATA
                println("DEBUG33-"+response.body()!!.STATUSMESSAGE)
                if (response.body()!!.STATUSMESSAGE == "MISSING AMOUNT"){
                    Toast.makeText(this@SellItemsActivity, "Nekog od proizvoda nema na skladištu", Toast.LENGTH_SHORT).show()

                }
                else if (response.body()!!.STATUSMESSAGE == "INVOICE GENERATED"){
                    idRacuna=response.body()!!.DATA!!.Id as Int
                    showDialog()
                }
            }
        })


    }

    private fun showDialog(){
        val dialogView = layoutInflater.run { inflate(R.layout.dialog_payment_method, null) }
        val dialogWindow = PopupWindow(
            dialogView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        dialogWindow.showAtLocation(linearLayout, Gravity.CENTER, 0, 0)
        dialogWindow.dimBehind()

        dialogView.btn_close_payment_dialog.setOnClickListener { dialogWindow.dismiss() }

        dialogView.btn_qr_code.setOnClickListener{
            startQR(idRacuna!!)
        }

        dialogView.btn_nfc.setOnClickListener{
        }
    }
}
