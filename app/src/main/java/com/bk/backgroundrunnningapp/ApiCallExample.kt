package com.bk.backgroundrunnningapp

import android.util.Log
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

fun callQuizApi() {
    val client = OkHttpClient()
    val request = Request.Builder()
        .url("https://opentdb.com/api.php?amount=2")
        .build()

    client.newCall(request).enqueue(
        (object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")
                    Log.i("response", response.body!!.string())
                }
            }
        }
                )
    )
}