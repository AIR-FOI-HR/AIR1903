package com.example.pop

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_registration_third.view.*
import kotlin.math.abs

class RegistrationThird : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view:View = inflater.inflate(R.layout.fragment_registration_third, container, false)
        view.button.setOnClickListener {
            startLoginActivity()
        }
        view.setOnTouchListener { _, event : MotionEvent ->
            if (event.action == MotionEvent.ACTION_DOWN) touchX = event.x
            if (event.action == MotionEvent.ACTION_UP)
                if(abs(touchX - event.x) > SWIPE_THRESHOLD) startLoginActivity()
            true
        }
        return view
    }

    private fun startLoginActivity() {
        val intent = Intent(activity,LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent);
        RegistrationData.Reset()
        activity!!.finish()
    }
}
