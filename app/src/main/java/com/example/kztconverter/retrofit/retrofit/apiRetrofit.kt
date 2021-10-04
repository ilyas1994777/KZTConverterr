package com.example.kztconverter.retrofit.retrofit

import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

class apiRetrofit {

val apiRetrofit : Retrofit = Retrofit.Builder()
    .baseUrl("https://v6.exchangerate-api.com/")
    .addConverterFactory(JacksonConverterFactory.create())
    .build()

    val apiClient : ApiClient = apiRetrofit.create(ApiClient::class.java)
}