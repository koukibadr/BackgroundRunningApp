package com.bk.backgroundrunnningapp.api

import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {
    @GET("api.php?amount=2")
    fun sendSMSMetrics(): Call<Any>
}