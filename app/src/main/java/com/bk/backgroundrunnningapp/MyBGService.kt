package com.bk.backgroundrunnningapp

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.util.Timer
import kotlin.concurrent.schedule


class MyBGService : Service() {
    companion object {
        private const val NOTIFICATION_ID = 112
        private const val PRIMARY_CHANNEL_ID = "primary_notification_channel"
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        Timer().schedule(0L, 1000L) {
            Log.i("MESSAGE FROM SERVICE","TEST MESSAGE")
        }
        startForeground()
        super.onCreate()
    }

    private fun startForeground(){
        val notificationIntent = Intent(this, MainActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(
            this, 0,
            notificationIntent, PendingIntent.FLAG_IMMUTABLE
        )

        startForeground(
            NOTIFICATION_ID, NotificationCompat.Builder(
                this,
                PRIMARY_CHANNEL_ID
            ) // don't forget create a notification channel first
                .setOngoing(true)
                .setSmallIcon(R.drawable.meme)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("Service is running background")
                .setContentIntent(pendingIntent)
                .build()
        )
    }

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
}