package com.example.pop.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.pop.*
import com.example.webservice.Common.Common
import com.example.webservice.Response.IMyAPI
import kotlinx.android.synthetic.main.fragment_registration_third.*
import kotlinx.android.synthetic.main.fragment_registration_third.view.*
import kotlin.math.abs


class RegistrationStep3Fragment : Fragment(), View.OnClickListener {


    private lateinit var mService: IMyAPI
    private var selectedRole: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (this.activity as RegistrationActivity).currentFragment=4
        mService = Common.api
        val view: View = inflater.inflate(R.layout.fragment_registration_third, container, false)
        view.btnNextStep.setOnClickListener { onClick(view) }
        view.radioGroupRole.setOnCheckedChangeListener { group, checkedId ->
            val selectedRadioButton = view.findViewById(checkedId) as RadioButton
            selectedRole = selectedRadioButton.tag.toString().toInt()
            Log.e("ULOGA", selectedRole.toString())
        }

        view.setOnTouchListener { v : View, event : MotionEvent ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val keyboard = HideKeyboard()
                keyboard.hideKeyboard(registrationStep3)
                touchX = event.x
            }
            if (event.action == MotionEvent.ACTION_UP) {
                if(abs(touchX - event.x) > SWIPE_THRESHOLD) {
                    if(touchX - event.x < 0) v.findNavController().navigateUp()
                }
            }
            true
        }
        return view
    }

    override fun onClick(v: View?) {
        RegistrationData.Uloga = selectedRole
        if(selectedRole == 3)
            v!!.findNavController().navigate(R.id.action_registrationStep3_to_registrationStep4Seller)
        else
            v!!.findNavController().navigate(R.id.action_registrationStep3_to_registrationStep4Buyer)
    }
}
