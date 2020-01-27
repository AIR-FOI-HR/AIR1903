package com.example.core

import android.content.Context
import android.content.Intent

interface PaymentInterface {
    fun createInvoice(context: Context, id:Int, intent: Intent)
    fun pay(context: Context, intent: Intent)
}