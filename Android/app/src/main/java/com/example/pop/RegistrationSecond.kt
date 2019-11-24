package com.example.pop

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_registration_second.view.*
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
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
