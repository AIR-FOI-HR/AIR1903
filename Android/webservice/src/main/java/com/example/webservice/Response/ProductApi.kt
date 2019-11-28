package com.example.webservice.Response

import io.reactivex.Observable
import retrofit2.http.GET
import com.example.database.Entities.ProductResponse

interface ProductApi {
    @GET("/pop/proizvodi.php")
    fun getProducts() : Observable<ProductResponse>
}