package com.example.pop.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.pop.*
import com.example.pop_sajamv2.Session
import com.example.webservice.Common.Common
import com.example.webservice.Model.RoleSetResponse
import com.example.webservice.Response.IMyAPI
import kotlinx.android.synthetic.main.fragment_registration_third.*
import kotlinx.android.synthetic.main.fragment_registration_third.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.abs


class RegistrationStep3Fragment : Fragment(), View.OnClickListener {


    private lateinit var mService: IMyAPI
    private var selectedRole: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (this.activity as RegistrationActivity).currentFragment=3
        mService = Common.api
        val view: View = inflater.inflate(R.layout.fragment_registration_third, container, false)
        view.btn_next_step.setOnClickListener { onClick(view) }
        view.radioGroupRole.setOnCheckedChangeListener { group, checkedId ->
            val selectedRadioButton = view.findViewById(checkedId) as RadioButton
            selectedRole = selectedRadioButton.tag.toString().toInt()
            //println("DEBUG33-ULOGA "+selectedRole.toString())
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
        //RegistrationData.Uloga = selectedRole
        if (selectedRole==0){
            Toast.makeText(activity, getString(R.string.toast_role_not_selected), Toast.LENGTH_SHORT).show()
            return
        }
        var mService = Common.api
        mService.setRole(Session.user.Token, RegistrationData.KorisnickoIme, true, selectedRole).enqueue(object: Callback<RoleSetResponse>{
            override fun onFailure(call: Call<RoleSetResponse>, t: Throwable) {
                Toast.makeText(activity, t!!.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<RoleSetResponse>,
                response: Response<RoleSetResponse>
            ) {
                if(response.body()!!.STATUSMESSAGE=="OWN ROLE SET") {
                    if(selectedRole == 3)
                        v!!.findNavController().navigate(R.id.action_registrationStep3_to_registrationStep4Seller)
                    else
                        v!!.findNavController().navigate(R.id.action_registrationStep3_to_registrationStep4Buyer)
                }
                else
                    Toast.makeText(activity, response.body()!!.STATUSMESSAGE, Toast.LENGTH_SHORT).show()
            }
        })
    }
}
