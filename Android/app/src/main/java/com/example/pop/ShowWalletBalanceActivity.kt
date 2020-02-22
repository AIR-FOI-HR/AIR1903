package com.example.pop

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.example.core.BaseActivity
import com.example.pop_sajamv2.Session
import com.example.webservice.Common.Common
import com.example.webservice.Model.WalletBalanceResponse
import kotlinx.android.synthetic.main.activity_show_wallet_balance.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShowWalletBalanceActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_wallet_balance)
        getBalance()
        refreshWalletBallance.setOnRefreshListener {
            getBalance()
            refreshWalletBallance.isRefreshing = false
        }
        btn_details.setOnClickListener{ showInvoices() }
    }

    private fun getBalance(){
        val api = Common.api
        if (Session.user.Id_Uloge == 1 || Session.user.Id_Uloge==2) {
            api.getWalletBalanceClient(Session.user.Token, true, Session.user.KorisnickoIme)
                .enqueue(object : Callback<WalletBalanceResponse> {
                    override fun onFailure(call: Call<WalletBalanceResponse>, t: Throwable) {
                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(
                        call: Call<WalletBalanceResponse>,
                        response: Response<WalletBalanceResponse>
                    ) {
                        walletBalance.text = response.body()!!.DATA + " HRK"
                    }
                })
        }
        else if (Session.user.Id_Uloge == 3){
            api.getWalletBalanceStore(Session.user.Token, true, Session.user.KorisnickoIme)
                .enqueue(object : Callback<WalletBalanceResponse> {
                    override fun onFailure(call: Call<WalletBalanceResponse>, t: Throwable) {
                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(
                        call: Call<WalletBalanceResponse>,
                        response: Response<WalletBalanceResponse>
                    ) {
                        if (response.body()!!.STATUSMESSAGE=="USER NOT IN STORE")
                            walletBalance.text = "Korisnik nije dio trgovine"
                        else
                            walletBalance.text = response.body()!!.DATA + " HRK"
                    }
                })
        }
    }

    private fun showInvoices() {
        val intent = Intent(this, ShowInvoicesActivity::class.java)
        startActivity(intent)
    }
}
