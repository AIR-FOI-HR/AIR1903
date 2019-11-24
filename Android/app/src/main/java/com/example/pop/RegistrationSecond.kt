package com.example.pop

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.webservice.Common.Common
import com.example.webservice.Model.ApiResponse
import com.example.webservice.Response.IMyAPI
import kotlinx.android.synthetic.main.fragment_registration_second.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrationSecond : Fragment(), View.OnClickListener {

    override fun onClick(v: View?) {
        if (!checkPassword(v?.layoutRegistrationInputPassword?.text.toString(),v?.layoutRegistrationInputPasswordConfirm?.text.toString())){
            Toast.makeText(activity,"Lozinke se ne podudaraju",Toast.LENGTH_SHORT).show()
        }
        else{
            RegistrationData.Lozinka = v?.layoutRegistrationInputPassword?.text.toString()
            if (RegistrationData.Lozinka==""){
                Toast.makeText(activity,"Lozinka ne smije biti prazna",Toast.LENGTH_SHORT).show()
                return
            }
            RegistrationData.Email = v?.layoutRegistrationInputEmail?.text.toString()
            if (RegistrationData.Email==""){
                Toast.makeText(activity,"Email ne smije biti prazan",Toast.LENGTH_SHORT).show()
                return
            }
            RegistrationData.KorisnickoIme = v?.layoutRegistrationInputUsername?.text.toString()
            if (RegistrationData.KorisnickoIme==""){
                Toast.makeText(activity,"Korisničko ime ne smije biti prazno",Toast.LENGTH_SHORT).show()
                return
            }
            mService.registerUser(RegistrationData.Ime, RegistrationData.Prezime, RegistrationData.Lozinka, RegistrationData.Email, RegistrationData.KorisnickoIme)
                .enqueue(object : Callback<ApiResponse> {
                    override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                        Toast.makeText(activity, t!!.message, Toast.LENGTH_SHORT).show()
                    }
                    override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                        if(!response!!.body()!!.STATUS){
                            Toast.makeText(activity,response!!.body()!!.STATUSMESSAGE,Toast.LENGTH_SHORT).show()
                        }
                        else {
                            Toast.makeText(activity, "Uspjesna registracija", Toast.LENGTH_SHORT).show()
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
