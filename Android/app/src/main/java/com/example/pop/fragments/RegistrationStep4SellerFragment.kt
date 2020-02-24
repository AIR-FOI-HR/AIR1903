package com.example.pop.fragments

import android.os.Bundle
import android.se.omapi.Session
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.pop.*
import com.example.webservice.Common.Common
import com.example.webservice.Model.OneStoreResponse
import kotlinx.android.synthetic.main.fragment_registration_fourth_seller.*
import kotlinx.android.synthetic.main.fragment_registration_fourth_seller.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.abs

class RegistrationStep4SellerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (this.activity as RegistrationActivity).currentFragment=4
        val view:View = inflater.inflate(R.layout.fragment_registration_fourth_seller, container, false)
        view.btn_registration_finish.setOnClickListener { createStore(view) }

        view.setOnTouchListener { _, event : MotionEvent ->
            if (event.action == MotionEvent.ACTION_DOWN) touchX = event.x
            if (event.action == MotionEvent.ACTION_UP)
                if(abs(touchX - event.x) > SWIPE_THRESHOLD) (activity as RegistrationActivity).startLoginActivity()
            true
        }
        return view
    }

    fun createStore(v:View?) {
        RegistrationData.Trgovina = input_registration_store_name.text.toString()

        var mService = Common.api

        mService.createStore(com.example.pop_sajamv2.Session.user.Token, RegistrationData.KorisnickoIme, true, RegistrationData.Trgovina)
            .enqueue(object: Callback<OneStoreResponse>{
                override fun onFailure(call: Call<OneStoreResponse>, t: Throwable) {
                    Toast.makeText(activity, t!!.message, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<OneStoreResponse>,
                    response: Response<OneStoreResponse>
                ) {
                    var resp = response.body()
                    if (resp!!.STATUSMESSAGE=="NO STORE NAME")
                        Toast.makeText(activity, getString(R.string.toast_store_name_blank), Toast.LENGTH_SHORT).show()
                    else if (resp!!.STATUSMESSAGE=="STORE ALREADY EXISTS")
                        Toast.makeText(activity, getString(R.string.toast_store_exists), Toast.LENGTH_SHORT).show()
                    else if (resp!!.STATUSMESSAGE=="STORE CREATED") {
                        v!!.findNavController().navigate(R.id.action_registrationStep4Seller_to_registrationStep5)
                    }
                    else
                        Toast.makeText(activity, resp!!.STATUSMESSAGE, Toast.LENGTH_SHORT).show()
                }
            })


        //
    }
}
