package com.example.webservice.Response

import com.example.webservice.Model.*
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
    fun getProducts(@Field("Readall") Readall: Boolean, @Field("Token") Token: String, @Field("KorisnickoIme") KorisnickoIme: String
    ) : Call<ProductResponse>

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
    fun addNewPackage(@Field("Token") Token: String,@Field("ADD") ADD: Boolean, @Field("Naziv") Naziv: String, @Field("Opis") Opis: String, @Field("Popust") Popust: String, @Field("KorisnickoIme") KorisnickoIme: String, @Field("KolicinaPaketa") KolicinaPaketa: Int) : Call<NewPackageResponse>

    @Multipart
    @POST("paketi.php")
    fun addNewPackageWithImage(@Part Token:MultipartBody.Part, @Part ADD: MultipartBody.Part, @Part Naziv:MultipartBody.Part, @Part Opis:MultipartBody.Part, @Part Popust:MultipartBody.Part, @Part file: MultipartBody.Part, @Part KorisnickoIme: MultipartBody.Part, @Part KolicinaPaketa: MultipartBody.Part): Call<NewPackageResponse>

    @Multipart
    @POST("paketi.php")
    fun updatePackageWithImage(@Part UPDATE:MultipartBody.Part, @Part Token:MultipartBody.Part, @Part Id:MultipartBody.Part, @Part Naziv:MultipartBody.Part, @Part Opis:MultipartBody.Part, @Part Popust:MultipartBody.Part, @Part Kolicina:MultipartBody.Part, @Part KorisnickoIme: MultipartBody.Part, @Part file: MultipartBody.Part): Call<NewPackageResponse>

    @FormUrlEncoded
    @POST("paketi.php")
    fun getAllPackages(@Field("Token") Token: String,@Field("GET") GET: Boolean, @Field("KorisnickoIme") KorisnickoIme: String) : Call<PackageResponse>


    @FormUrlEncoded
    @POST("paketi.php")
    fun deletePackage(@Field("Token") Token: String,@Field("DELETE") DELETE: Boolean, @Field("Id") Id: String) : Call<NewPackageResponse>

    @FormUrlEncoded
    @POST("paketi.php")
    fun updatePackage(@Field("Token") Token: String,@Field("UPDATE") UPDATE: Boolean, @Field("Id") Id: Int, @Field("Naziv") Naziv: String, @Field("Opis") Opis: String, @Field("Kolicina") Kolicina: String, @Field("Popust") Popust: String, @Field("Slika")Slika: String , @Field("KorisnickoIme") KorisnickoIme: String) : Call<NewPackageResponse>

    @FormUrlEncoded
    @POST("paketi.php")
    fun getOnePackageContents(@Field("Token") Token: String, @Field("GETONE") GETONE: Boolean, @Field("Id") Id: String):Call<ProductResponse>

    @FormUrlEncoded
    @POST("paketi.php")
    fun addToPackage(@Field("Token") Token: String, @Field("ADDTOPACKET") ADDTOPACKET: Boolean, @Field("Id_Paket") Id_Paket: String, @Field("Id_Proizvod[]") Id_Proizvod: ArrayList<Int>, @Field("Kolicina[]") Kolicina: ArrayList<String>):Call<PackageResponse>

    @FormUrlEncoded
    @POST("novcanik.php")
    fun getWalletBalanceClient(@Field("Token") Token: String, @Field("GETCLIENT") GET: Boolean, @Field("KorisnickoIme") KorisnickoIme: String) : Call<WalletBalanceResponse>

    @FormUrlEncoded
    @POST("novcanik.php")
    fun getWalletBalanceStore(@Field("Token") Token: String, @Field("GETSTORE") GET: Boolean, @Field("KorisnickoIme") KorisnickoIme: String) : Call<WalletBalanceResponse>

    @FormUrlEncoded
    @POST("racuni.php")
    fun getAllInvoices(@Field("Token") Token: String, @Field("Readall") Readall: Boolean, @Field("KorisnickoIme") KorisnickoIme: String) : Call<InvoiceResponse>

    @FormUrlEncoded
    @POST("racuni.php")
    fun getOneInvoice(@Field("Token") Token: String, @Field("Readone") Readone: Boolean, @Field("KorisnickoIme") KorisnickoIme: String, @Field("Id_Racuna") Id_Racuna: String) : Call<OneInvoiceResponse>

    @FormUrlEncoded
    @POST("racuni.php")
    fun generateInvoice(@Field("Token") Token: String, @Field("GENERATESALE") GENERATESALE: Boolean, @Field("KorisnickoIme") KorisnickoIme: String, @Field("PopustRacuna") PopustRacuna: String, @Field("Itemi[]") Itemi: ArrayList<Int>, @Field("Kolicine[]") Kolicine: ArrayList<String>) : Call<OneInvoiceResponse>

    @FormUrlEncoded
    @POST("racuni.php")
    fun finalizeInvoice(@Field("Token") Token: String, @Field("CONFIRMSALE") CONFIRMSALE: Boolean, @Field("KorisnickoIme") KorisnickoIme: String, @Field("Id_Racuna") Id_Racuna: Int) : Call<OneInvoiceResponse>
    
    /*@Multipart
    @POST("/upload")
    fun uploadImage(@Part file: MultipartBody.Part, @Part("name") requestBody: RequestBody) : Call<ResponseBody>
*/
}

