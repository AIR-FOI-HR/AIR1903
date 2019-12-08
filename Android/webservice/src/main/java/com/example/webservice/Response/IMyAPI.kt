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

    @FormUrlEncoded
    @POST("proizvodi.php")
    fun addNewProductNoImage (@Field("Token") Token: String, @Field("Naziv") Naziv: String, @Field("Opis") Opis: String, @Field("Cijena") Cijena: String?): Call<NewProductResponse>

    @Multipart
    @POST("proizvodi.php")
    fun addNewProduct(@Part Token:MultipartBody.Part, @Part Naziv:MultipartBody.Part, @Part Opis:MultipartBody.Part, @Part Cijena:MultipartBody.Part, @Part file: MultipartBody.Part): Call<NewProductResponse>


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

