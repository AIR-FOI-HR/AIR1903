package com.example.pop

import android.content.Context

interface PaymentInterface {
    fun createInvoice(context: Context, id:Int)
    fun pay(context: Context)
}