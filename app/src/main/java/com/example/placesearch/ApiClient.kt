package com.example.placesearch

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

open class ApiClient {

    companion object {
        var base_url = "https://maps.googleapis.com/maps/api/"
        val GOOGLE_PLACE_API_KEY : String  = "AIzaSyDAu8Svsar7h6j49nl3vAqesDycnoLIJjE"

        val okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .readTimeout(30,TimeUnit.SECONDS)
            .connectTimeout(30,TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                HttpLoggingInterceptor.Level.BODY
            })
            .build()

        fun getService() : ApiInterface
        {
            val retrofit = Retrofit.Builder().baseUrl(base_url).client(okHttpClient).addConverterFactory(GsonConverterFactory.create()).build()
            return retrofit.create(ApiInterface::class.java)
        }
    }
}