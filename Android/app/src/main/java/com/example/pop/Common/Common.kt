package com.example.pop.Common

import com.example.pop.Response.IMyAPI
import com.example.pop.Response.RetrofitClient

object Common {

    val BASE_URL = "https://cortex.foi.hr/pop/"

    val api: IMyAPI
        get() = RetrofitClient.getClient(BASE_URL).create(IMyAPI::class.java)
}