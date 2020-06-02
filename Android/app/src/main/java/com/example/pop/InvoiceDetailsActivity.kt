package com.example.pop

import android.content.Intent
import android.os.Bundle
import com.example.core.BaseActivity
import com.example.pop.adapters.InvoiceItemAdapter
import com.example.pop_sajamv2.Session
import com.example.webservice.Model.Invoice
import kotlinx.android.synthetic.main.activity_invoice_details.*

class InvoiceDetailsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invoice_details)
        val invoice : Invoice = intent.getSerializableExtra("invoice") as Invoice

        layoutInvoiceDetailsId.text = invoice.Id.toString()
        layoutInvoiceDetailsLocation.text = invoice.MjestoIzdavanja
        layoutInvoiceDetailsDate.text = invoice.DatumIzdavanja
        layoutInvoiceDetailsSeller.text = invoice.Trgovina
        layoutInvoiceDetailsBuyer.text = invoice.Ime_Klijenta
        layoutInvoiceDetailsTotal.text = "%.2f".format(invoice.ZavrsnaCijena!!.toDouble())
        layoutInvoiceDetailsInvoiceDiscountValue.text = invoice.PopustRacuna+" %"
        layoutInvoiceDetailsInvoiceBeforeDiscountValue.text = invoice.CijenaRacuna
        layoutInvoiceDetailsInvoiceDiscountAmountValue.text = invoice.IznosPopustaRacuna

        val invoiceItemsAdapter = InvoiceItemAdapter()
        layoutInvoiceDetailsRecycler.adapter = invoiceItemsAdapter
        invoiceItemsAdapter.data = invoice.Stavke!!
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@InvoiceDetailsActivity, ShowInvoicesActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }
}
