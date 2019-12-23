package com.example.pop.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.pop.*
import com.example.webservice.Common.Common
import com.example.webservice.Model.ApiResponseUser
import com.example.webservice.Response.IMyAPI
import kotlinx.android.synthetic.main.fragment_registration_second.*
import kotlinx.android.synthetic.main.fragment_registration_second.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.abs

class RegistrationStep2Fragment : Fragment(), View.OnClickListener {

    override fun onClick(v: View?) {
        if (!checkPassword(v?.layoutRegistrationInputPassword?.text.toString(),v?.layoutRegistrationInputPasswordConfirm?.text.toString())){
            Toast.makeText(activity, R.string.toastPasswordsDontMatch,Toast.LENGTH_SHORT).show()
        }
        else{
            RegistrationData.Lozinka = v?.layoutRegistrationInputPassword?.text.toString()
            if (RegistrationData.Lozinka ==""){
                Toast.makeText(activity, R.string.toastEmptyPassword, Toast.LENGTH_SHORT).show()
                return
            }
            RegistrationData.Email = v?.layoutRegistrationInputEmail?.text.toString()
            if (RegistrationData.Email ==""){
                Toast.makeText(activity, R.string.toastEmptyEmail, Toast.LENGTH_SHORT).show()
                return
            }
            RegistrationData.KorisnickoIme = v?.layoutRegistrationInputUsername?.text.toString()
            if (RegistrationData.KorisnickoIme ==""){
                Toast.makeText(activity, R.string.toastEmptyUsername, Toast.LENGTH_SHORT).show()
                return
            }
            mService.registerUser(
                RegistrationData.Ime,
                RegistrationData.Prezime,
                RegistrationData.Lozinka,
                RegistrationData.Email,
                RegistrationData.KorisnickoIme
            )
                .enqueue(object : Callback<ApiResponseUser> {
                    override fun onFailure(call: Call<ApiResponseUser>, t: Throwable) {
                        Toast.makeText(activity, t!!.message, Toast.LENGTH_SHORT).show()
                    }
                    override fun onResponse(call: Call<ApiResponseUser>, response: Response<ApiResponseUser>) {
                        if(!response!!.body()!!.STATUS){
                            Toast.makeText(activity,response!!.body()!!.STATUSMESSAGE,Toast.LENGTH_SHORT).show()
                        }
                        else {
                            Toast.makeText(activity,
                                R.string.toastRegistrationSuccess, Toast.LENGTH_SHORT).show()
                            v!!.findNavController().navigate(R.id.action_registrationSecond_to_registrationThird)
                        }
                    }
                })
        }
    }

    internal lateinit var mService: IMyAPI

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mService = Common.api
        val view: View = inflater.inflate(R.layout.fragment_registration_second, container, false)
        view.layoutRegistrationButtonRegister.setOnClickListener {
            onClick(view)
        }

        val keyboard = HideKeyboard()
        view.registrationSecond.setOnClickListener{keyboard.hideKeyboard(registrationSecond)}
        view.setOnTouchListener { v : View, event : MotionEvent ->
            if (event.action == MotionEvent.ACTION_DOWN) touchX = event.x
            if (event.action == MotionEvent.ACTION_UP) {
                if(abs(touchX - event.x) > SWIPE_THRESHOLD) {
                    if(touchX - event.x < 0) v.findNavController().navigateUp()
                }
            }
            true
        }
        return view
    }

    private fun checkPassword(
        Lozinka: String,
        PotvrdaLozinke: String
    ):Boolean{
        if (Lozinka==PotvrdaLozinke) {
            return true
        }
        return false
    }
}
