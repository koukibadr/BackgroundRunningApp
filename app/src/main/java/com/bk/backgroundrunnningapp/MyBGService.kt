package com.bk.backgroundrunnningapp

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.provider.Telephony
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.util.Timer
import kotlin.concurrent.schedule


class MyBGService : Service(), MessageListenerInterface {
    companion object {
        private const val NOTIFICATION_ID = 112
        private const val PRIMARY_CHANNEL_ID = "primary_notification_channel"
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        MessageBroadcastReceiver.bindListener(this)
        var receiver = MessageBroadcastReceiver()
        IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION).also {
            registerReceiver(receiver, it)
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

    override fun messageReceived(message: String): String {
        //TODO send message via API POST
        Toast.makeText(applicationContext,message,Toast.LENGTH_SHORT).show()
        return message
    }
}