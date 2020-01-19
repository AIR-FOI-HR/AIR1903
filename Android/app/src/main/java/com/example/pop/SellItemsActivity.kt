package com.example.pop

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import com.example.pop.adapters.SellItemsAdapter
import com.example.qr.QRCode
import com.example.webservice.Model.Item
import com.example.webservice.Model.PackageClass
import com.example.webservice.Model.Product
import kotlinx.android.synthetic.main.activity_sell_items.*
import kotlinx.android.synthetic.main.dialog_payment_method.view.*
import kotlinx.android.synthetic.main.sell_item_list.*
import java.lang.System.out

class SellItemsActivity : AppCompatActivity() {

    private var itemsList : List<Item> = listOf()
    var totalValue: Double = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sell_items)

        itemsList = (intent.getSerializableExtra("items") as ItemsWrapper).getItems()

        val adapter = SellItemsAdapter()
        layoutSellItemsRecycler.adapter = adapter
        adapter.data = itemsList

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
}
