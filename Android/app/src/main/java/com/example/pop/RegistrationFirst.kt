package com.example.pop

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_registration_first.view.*

class RegistrationFirst : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_registration_first, container, false)
        view.layoutRegistrationButtonNext.setOnClickListener {
            it.findNavController().navigate(R.id.action_registrationFirst_to_registrationSecond)
        }
        return view
    }
}
