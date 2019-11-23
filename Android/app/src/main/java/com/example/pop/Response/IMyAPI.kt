package com.example.pop.Response

import com.example.pop.Model.ApiResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface IMyAPI {
    @FormUrlEncoded

    @POST("registracija.php")
    fun registerUser(@Field("Ime") Ime:String, @Field("Prezime") Prezime:String, @Field("LozinkaTest") LozinkaTest:String, @Field("Email") Email:String, @Field("KorisnickoIme") KorisnickoIme:String): Call<ApiResponse>


}