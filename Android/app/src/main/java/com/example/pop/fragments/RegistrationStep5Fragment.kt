package com.example.pop.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pop.R
import com.example.pop.RegistrationActivity
import kotlinx.android.synthetic.main.fragment_registration_fifth.view.*

class RegistrationStep5Fragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (this.activity as RegistrationActivity).currentFragment=5
        val view:View = inflater.inflate(R.layout.fragment_registration_fifth, container, false)
        view.btn_next_step.setOnClickListener {
            (activity as RegistrationActivity).startLoginActivity()
        }
        return view
    }
}
