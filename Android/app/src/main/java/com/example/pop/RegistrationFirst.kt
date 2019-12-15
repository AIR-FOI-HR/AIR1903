package com.example.pop

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_registration_first.*
import kotlinx.android.synthetic.main.fragment_registration_first.view.*
import kotlin.math.abs


class RegistrationFirst : Fragment(), View.OnClickListener {
    override fun onClick(v: View?) {
        RegistrationData.Ime = v?.layoutRegistrationInputName?.text.toString()
        if (RegistrationData.Ime == ""){
            Toast.makeText(activity, R.string.toastEmptyName, Toast.LENGTH_SHORT).show()
            return
        }

        RegistrationData.Prezime = v?.layoutRegistrationInputSurname?.text.toString()
        if (RegistrationData.Prezime==""){
            Toast.makeText(activity, R.string.toastEmptySurname, Toast.LENGTH_SHORT).show()
            return
        }

        v!!.findNavController().navigate(R.id.action_registrationFirst_to_registrationSecond)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_registration_first, container, false)
        view.layoutRegistrationButtonNext.setOnClickListener {
            onClick(view)
        }

        val keyboard = HideKeyboard()
        view.regFirst.setOnClickListener{keyboard.hideKeyboard(regFirst)}
        view.setOnTouchListener { v : View, event : MotionEvent ->
            if (event.action == MotionEvent.ACTION_DOWN) touchX = event.x
            if (event.action == MotionEvent.ACTION_UP) {
                if(abs(touchX - event.x) > SWIPE_THRESHOLD) {
                    if(touchX - event.x > 0) onClick(v)
                }
            }
            true
        }
        return view
    }


}
