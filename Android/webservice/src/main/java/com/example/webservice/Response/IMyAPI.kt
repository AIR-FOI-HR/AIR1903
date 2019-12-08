package com.example.webservice.Response

import com.example.webservice.Model.ProductResponse
import com.example.webservice.Model.ApiResponseUser
import com.example.webservice.Model.NewProductResponse
import io.reactivex.Observable
import okhttp3.*
import retrofit2.Call
import retrofit2.http.*

interface IMyAPI {
    @FormUrlEncoded
    @POST("registracija.php")
    fun registerUser(@Field("Ime") Ime: String, @Field("Prezime") Prezime: String, @Field("Lozinka") Lozinka: String, @Field("Email") Email: String, @Field("KorisnickoIme") KorisnickoIme: String): Call<ApiResponseUser>

    @FormUrlEncoded
    @POST("login.php")
    fun storeUser(@Field("KorisnickoIme") KorisnickoIme: String, @Field("Lozinka") Lozinka: String) : Call<ApiResponseUser>

    @FormUrlEncoded
    @POST("proizvodi.php")
    fun getProducts(@Field("Readall") Readall: Boolean, @Field("Token") Token: String) : Call<ProductResponse>

    @Multipart
    @FormUrlEncoded
    @POST("proizvodi.php")
    fun addNewProduct(@Query ("Token") Token: String, @Query ("Naziv") Naziv: String, @Query ("Opis") Opis: String, @Query ("Cijena") Cijena: String?, @Part file: MultipartBody.Part, @Part("Slika") requestBody: RequestBody): Call<NewProductResponse>

    @FormUrlEncoded
    @POST("proizvodi.php")
    fun editProduct(@Field("Token")Token: String, @Field("Id")Id: Int, @Field("Naziv")Naziv: String, @Field("Opis") Opis: String, @Field("Cijena") Cijena: String?, @Field("Slika")Slika: String): Call<NewProductResponse>

    @FormUrlEncoded
    @POST("proizvodi.php")
    fun deleteProduct(@Field("Token")Token: String, @Field("Id")Id: Int) : Call<NewProductResponse>

    /*@Multipart
    @POST("/upload")
    fun uploadImage(@Part file: MultipartBody.Part, @Part("name") requestBody: RequestBody) : Call<ResponseBody>
*/
}

