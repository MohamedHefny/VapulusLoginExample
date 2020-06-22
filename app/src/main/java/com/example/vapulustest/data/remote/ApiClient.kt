package com.example.vapulustest.data.remote

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private val okHttpClient = OkHttpClient.Builder().build()

    /**
     * Build Retrofit object and create ApiService from it.
     * */
    val apiServices: ApiServices
        get() = Retrofit.Builder()
            .baseUrl("https://api.vapulus.com")
            .addConverterFactory(
                GsonConverterFactory
                    .create(GsonBuilder().setLenient().create())
            )
            .client(okHttpClient)
            .build().create(ApiServices::class.java)
}