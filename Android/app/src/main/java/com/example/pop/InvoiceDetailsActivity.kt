package com.example.pop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pop.adapters.InvoiceItemAdapter
import com.example.pop_sajamv2.Session
import com.example.webservice.Model.Invoice
import kotlinx.android.synthetic.main.activity_invoice_details.*

class InvoiceDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invoice_details)
        val invoice : Invoice = intent.getSerializableExtra("invoice") as Invoice

        layoutInvoiceDetailsId.text = invoice.Id.toString()
        layoutInvoiceDetailsLocation.text = invoice.MjestoIzdavanja
        layoutInvoiceDetailsDate.text = invoice.DatumIzdavanja
        layoutInvoiceDetailsSeller.text = invoice.Trgovina
        layoutInvoiceDetailsBuyer.text = invoice.Ime_Klijenta
        layoutInvoiceDetailsTotal.text = invoice.ZavrsnaCijena
        layoutInvoiceDetailsInvoiceDiscountValue.text = invoice.PopustRacuna+" %"
        layoutInvoiceDetailsInvoiceBeforeDiscountValue.text = invoice.CijenaRacuna
        layoutInvoiceDetailsInvoiceDiscountAmountValue.text = invoice.IznosPopustaRacuna

        val invoiceItemsAdapter = InvoiceItemAdapter()
        layoutInvoiceDetailsRecycler.adapter = invoiceItemsAdapter
        invoiceItemsAdapter.data = invoice.Stavke!!
    }

    override fun onBackPressed() {
        super.onBackPressed()
        var intent:Intent
        if (Session.user.Id_Uloge == 3){
            intent=Intent(this@InvoiceDetailsActivity, MainMenuSeller::class.java)
        }
        else{
            intent=Intent(this@InvoiceDetailsActivity, MainMenuBuyer::class.java)
        }
        startActivity(intent)
        finishAffinity()

    }
}
