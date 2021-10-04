package com.example.kztconverter.retrofit.retrofit


import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET

interface ApiClient {
    @GET("/v6/4c3d5c83194f44d33534f3b1/latest/USD")
    fun getUpcomingEvent(): Call<ResponseBody>
}