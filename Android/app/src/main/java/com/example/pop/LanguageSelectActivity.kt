package com.example.pop

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import com.example.core.BaseActivity
import com.example.pop_sajamv2.Session
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.activity_tab_layout.*
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.MotionEvent
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import com.example.pop.fragments.RegistrationStep3Fragment
import com.example.webservice.Common.Common
import com.example.webservice.Model.RoleSetResponse
import kotlinx.android.synthetic.main.activity_language_select.*
import kotlinx.android.synthetic.main.fragment_registration_third.*
import kotlinx.android.synthetic.main.fragment_registration_third.radioGroupRole
import kotlinx.android.synthetic.main.fragment_registration_third.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.abs

class LanguageSelectActivity : BaseActivity() {
    private var selectedLanguage: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language_select)
        if (Session.user.Jezik==0){
            radioGroupRole.check(R.id.RadioEnglish)
            selectedLanguage=0
        }
        else if (Session.user.Jezik==1){
            radioGroupRole.check(R.id.RadioCroatian)
            selectedLanguage=1
        }

        radioGroupRole.setOnCheckedChangeListener { group, checkedId ->
            val selectedRadioButton = findViewById(checkedId) as RadioButton
            selectedLanguage = selectedRadioButton.tag.toString().toInt()
        }

        btn_choose_language.setOnClickListener{chooseLanguage()}
    }

    fun chooseLanguage(){
        var intent:Intent
        if (Session.user.Id_Uloge==1) {
            intent = Intent(this, MainMenuBuyer::class.java)
        }
        else{
            intent = Intent(this, MainMenuSeller::class.java)
        }
        if (selectedLanguage==Session.user.Jezik){
            startActivity(intent)
            finishAffinity()
        }
        else {
            var mService = Common.api
            mService.setLanguage(Session.user.Token,Session.user.KorisnickoIme,true,selectedLanguage).enqueue(object :
                Callback<RoleSetResponse> {
                override fun onFailure(call: Call<RoleSetResponse>, t: Throwable) {
                    Toast.makeText(this@LanguageSelectActivity, t!!.message, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<RoleSetResponse>,
                    response: Response<RoleSetResponse>
                ) {
                    if (response.body()!!.STATUSMESSAGE == "LANGUAGE SET") {
                        Session.user.Jezik=selectedLanguage
                        startActivity(intent)
                        finishAffinity()
                    } else
                        Toast.makeText(this@LanguageSelectActivity,response.body()!!.STATUSMESSAGE,Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

}
