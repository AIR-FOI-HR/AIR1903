package com.example.pop.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.pop.*
import kotlinx.android.synthetic.main.fragment_registration_fourth_seller.*
import kotlinx.android.synthetic.main.fragment_registration_fourth_seller.view.*
import kotlin.math.abs

class RegistrationStepSellerFragment : Fragment(), View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (this.activity as RegistrationActivity).currentFragment=4
        val view:View = inflater.inflate(R.layout.fragment_registration_fourth_seller, container, false)
        view.btn_registration_finish.setOnClickListener { onClick(view) }

        view.setOnTouchListener { _, event : MotionEvent ->
            if (event.action == MotionEvent.ACTION_DOWN) touchX = event.x
            if (event.action == MotionEvent.ACTION_UP)
                if(abs(touchX - event.x) > SWIPE_THRESHOLD) (activity as RegistrationActivity).startLoginActivity()
            true
        }
        return view
    }

    override fun onClick(v: View?) {
        RegistrationData.Trgovina = input_registration_store_name.text.toString()
        v!!.findNavController().navigate(R.id.action_registrationStep4Seller_to_registrationStep5)
    }
}
