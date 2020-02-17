package com.example.core

import android.content.res.Configuration
import android.view.ContextThemeWrapper
import androidx.appcompat.app.AppCompatActivity
import com.example.pop_sajamv2.Session
import java.util.*

open class BaseActivity : AppCompatActivity() {

    companion object {
        public var dLocale: Locale? = null
    }

    init {
        updateConfig(this)
    }

    fun updateConfig(wrapper: ContextThemeWrapper) {
        if (Session.user.Jezik==1){
            dLocale =Locale("hr")
        }
        else {
            dLocale =Locale("")
        }
        if(dLocale ==Locale("") ) // Do nothing if dLocale is null
            return

        Locale.setDefault(dLocale)
        val configuration = Configuration()
        configuration.setLocale(dLocale)
        wrapper.applyOverrideConfiguration(configuration)
    }
}
