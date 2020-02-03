package com.example.core

import com.example.webservice.Model.Invoice

interface InvoiceListener {
    fun onInvoiceLoaded(invoice:Invoice)
}