package com.example.pop

import android.content.Context
import android.content.Intent

class QRPayment : PaymentInterface {
    override fun startPayment(context: Context, id: Int) {
        val intent = Intent(context, QRCodeActivity::class.java)
        intent.putExtra("Total", id.toString())
        context.startActivity(intent)
    }
}