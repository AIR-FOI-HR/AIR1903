package com.example.core

import android.content.Context
import android.content.Intent
import com.example.webservice.Model.Invoice
import com.example.webservice.Model.OneInvoiceResponse

interface PaymentInterface {
    fun pay(context: Context): OneInvoiceResponse
}