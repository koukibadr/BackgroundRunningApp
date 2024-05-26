package com.bk.backgroundrunnningapp

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
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
            Log.i("SERVICE","A MESSAGE FROM SERVICE")
        }
        startForeground()
        super.onCreate()
    }

    private fun startForeground(){
        val notificationIntent = Intent(this, MainActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(
            this, 0,
            notificationIntent, 0
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
}