package com.bk.backgroundrunnningapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class RestartServiceBroadcast: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.startService(Intent(context.applicationContext, MyBGService::class.java))
    }
}