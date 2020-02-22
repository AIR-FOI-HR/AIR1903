package com.example.core

import android.app.Application
import android.content.Context


class BaseApp : Application() {
    companion object {
        lateinit var instance: BaseApp private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

}