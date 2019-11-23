package com.example.webservice.Common

import com.example.webservice.Response.IMyAPI
import com.example.webservice.Response.RetrofitClient

object Common {

    val BASE_URL = "https://cortex.foi.hr/pop/"

    val api: IMyAPI
        get() = RetrofitClient.getClient(BASE_URL).create(IMyAPI::class.java)
}