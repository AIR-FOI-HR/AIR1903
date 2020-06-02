package com.example.android_code

import CodePayment
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.webservice.Model.Invoice
import kotlinx.android.synthetic.main.activity_code_payment.*

class CodePaymentActivity : AppCompatActivity() {

    var invoiceCode: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_code_payment)
        btn_next_step.setOnClickListener { finishPayment() }
        animation_loading.addAnimatorListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                super.onAnimationStart(animation)
                animation_loading.setMinAndMaxFrame(0,15)
            }
        })
    }

    override fun onBackPressed() {
        finish()
    }

    private fun finishPayment() {
        invoiceCode = input_payment_code.text.toString()
        if (invoiceCode.isNotEmpty()) {
            val payment = CodePayment(invoiceCode)
            val response = payment.pay(this)
            val invoice: Invoice

            when (response.STATUSMESSAGE) {
                "INVOICE FINALIZED" -> {
                    invoice = response.DATA!! as Invoice
                    intent.putExtra("invoice", invoice)
                    animation_loading.addAnimatorListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationRepeat(animation: Animator?) {
                            super.onAnimationRepeat(animation)
                            animation_loading.setMaxFrame(60)
                            animation_loading.repeatCount = 1
                        }

                        override fun onAnimationEnd(animation: Animator?) {
                            super.onAnimationEnd(animation)
                            val intent = intent.extras!!.get("detailsIntent") as Intent
                            intent.putExtra("invoice", invoice)
                            startActivity(intent)
                            finishAffinity()
                        }
                    })

                }
                "MISSING AMOUNT" -> {
                    Toast.makeText(this, getString(com.example.core.R.string.toast_out_of_stock), Toast.LENGTH_SHORT).show()
                }
                "MISSING BALANCE" -> {
                    Toast.makeText(this, getString(com.example.core.R.string.toast_out_of_balance), Toast.LENGTH_SHORT).show()
                }
                "NO BUYING FROM OWN STORE" -> {
                    Toast.makeText(this, getString(com.example.core.R.string.toast_buy_from_own_store), Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(this, getString(com.example.core.R.string.toast_payment_code_invalid), Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, getString(com.example.core.R.string.toast_payment_code_empty), Toast.LENGTH_SHORT).show()
        }

    }
}
