package com.example.webservice.Response

import com.example.webservice.ItemJsonDeserializer
import com.example.webservice.Model.Item
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private var retrofit: Retrofit? = null

    fun getClient(baseUrl: String): Retrofit {
        if (retrofit == null) {

            /*val logging = HttpLoggingInterceptor( object: HttpLoggingInterceptor.Logger{
                override fun log(message: String) {
                    println("DEBUG-intercept: "+message)
                }
            })
            logging.level = (HttpLoggingInterceptor.Level.BODY)*/
            val client = OkHttpClient.Builder()
            //client.addInterceptor(logging)


            var builder = GsonBuilder().registerTypeAdapter(Item::class.java, ItemJsonDeserializer()).create()
            retrofit = Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(builder))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client.build())
                .build()
        }
        return retrofit!!
    }
}