package com.example.pop

import android.content.Context

interface PaymentInterface {
    fun startPayment(context: Context, id:Int)
}