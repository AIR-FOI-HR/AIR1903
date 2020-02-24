package com.example.pop.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.example.pop.R
import com.example.pop.RegistrationActivity
import com.example.pop.SWIPE_THRESHOLD
import com.example.pop.touchX
import kotlinx.android.synthetic.main.fragment_registration_third.view.*
import kotlin.math.abs

class RegistrationStep3Fragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (this.activity as RegistrationActivity).currentFragment=3
        val view:View = inflater.inflate(R.layout.fragment_registration_third, container, false)
        view.button.setOnClickListener {
            (activity as RegistrationActivity).startLoginActivity()
        }
        view.setOnTouchListener { _, event : MotionEvent ->
            if (event.action == MotionEvent.ACTION_DOWN) touchX = event.x
            if (event.action == MotionEvent.ACTION_UP)
                if(abs(touchX - event.x) > SWIPE_THRESHOLD) (activity as RegistrationActivity).startLoginActivity()
            true
        }
        return view
    }
}
