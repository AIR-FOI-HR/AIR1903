package com.example.webservice.Response

import com.example.webservice.Model.ProductResponse
import com.example.webservice.Model.ApiResponseUser
import com.example.webservice.Model.NewPackageResponse
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
    fun getProducts(@Field("Readall") Readall: Boolean, @Field("Token") Token: String, @Field("KorisnickoIme") KorisnickoIme: String) : Call<ProductResponse>

    @FormUrlEncoded
    @POST("proizvodi.php")
    fun addNewProductNoImage (@Field("Token") Token: String, @Field("Naziv") Naziv: String, @Field("Opis") Opis: String, @Field("Cijena") Cijena: String?, @Field("Kolicina") Kolicina:Int, @Field("KorisnickoIme") KorisnickoIme: String): Call<NewProductResponse>

    @Multipart
    @POST("proizvodi.php")
    fun addNewProduct(@Part Token:MultipartBody.Part, @Part Naziv:MultipartBody.Part, @Part Opis:MultipartBody.Part, @Part Cijena:MultipartBody.Part, @Part Kolicina:MultipartBody.Part, @Part file: MultipartBody.Part, @Part KorisnickoIme: MultipartBody.Part): Call<NewProductResponse>

    @FormUrlEncoded
    @POST("proizvodi.php")
    fun editProductNoImage(@Field("Edit") Edit: Boolean, @Field("Token")Token: String, @Field("Id")Id: Int, @Field("Naziv")Naziv: String, @Field("Opis") Opis: String, @Field("Cijena") Cijena: String?, @Field("Kolicina") Kolicina:Int , @Field("Slika")Slika: String , @Field("KorisnickoIme") KorisnickoIme: String): Call<NewProductResponse>

    @Multipart
    @POST("proizvodi.php")
    fun editProduct(@Part Edit:MultipartBody.Part, @Part Token:MultipartBody.Part, @Part Id:MultipartBody.Part, @Part Naziv:MultipartBody.Part, @Part Opis:MultipartBody.Part, @Part Cijena:MultipartBody.Part, @Part Kolicina:MultipartBody.Part, @Part file: MultipartBody.Part, @Part KorisnickoIme: MultipartBody.Part): Call<NewProductResponse>

    @FormUrlEncoded
    @POST("proizvodi.php")
    fun deleteProduct(@Field("Token")Token: String, @Field("Id")Id: Int) : Call<NewProductResponse>


    @FormUrlEncoded
    @POST("paketi.php")
    fun addNewPackage(@Field("Token") Token: String,@Field("ADD") ADD: Boolean, @Field("NazivPaketa") NazivPaketa: String, @Field("Id_Proizvoda") Id_Proizvoda: String, @Field("Kolicina") Kolicina: String, @Field("Popust") Popust: String) : Call<NewPackageResponse>
    /*@Multipart
    @POST("/upload")
    fun uploadImage(@Part file: MultipartBody.Part, @Part("name") requestBody: RequestBody) : Call<ResponseBody>
*/
}

