package com.bk.backgroundrunnningapp

import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.provider.Telephony
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.bk.backgroundrunnningapp.api.ApiInterface
import com.bk.backgroundrunnningapp.api.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class MyBGService : Service(), MessageListenerInterface {
    companion object {
        private const val NOTIFICATION_ID = 112
        private const val PRIMARY_CHANNEL_ID = "primary_notification_channel"
        private lateinit var apiInterface: ApiInterface
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        apiInterface = RetrofitInstance.getInstance().create(ApiInterface::class.java)
        MessageBroadcastReceiver.bindListener(this)
        var receiver = MessageBroadcastReceiver()
        IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION).also {
            registerReceiver(receiver, it)
        }
        startForeground()
        return START_STICKY
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
        apiInterface.sendSMSMetrics().enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: retrofit2.Response<Any>) {
                if (response.isSuccessful && response.body() !=null){
                    val sharedPreference =  applicationContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                    var editor = sharedPreference.edit()
                    editor.putString("lastSynchroToBackend", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                    editor.apply()
                }
            }
            override fun onFailure(call: Call<Any>, t: Throwable) {
                t.printStackTrace()
            }}
        )
        val sharedPreference =  applicationContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        var editor = sharedPreference.edit()
        editor.putString("lastSMSSynchro", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
        editor.apply()
        Toast.makeText(applicationContext,message,Toast.LENGTH_SHORT).show()
        return message
    }
}