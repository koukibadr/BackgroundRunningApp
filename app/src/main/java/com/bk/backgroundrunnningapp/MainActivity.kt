package com.bk.backgroundrunnningapp

import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Telephony
import android.util.Log
import android.widget.Toast

class MainActivity : AppCompatActivity(), MessageListenerInterface {
    lateinit var receiver: MessageBroadcastReceiver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MessageBroadcastReceiver.bindListener(this)
        receiver = MessageBroadcastReceiver()
        // Intent Filter is useful to determine which apps wants to receive
        // which intents,since here we want to respond to change of
        // airplane mode
        IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION).also {
            registerReceiver(receiver, it)
        }
    }

    override fun messageReceived(message: String): String {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
        return message
    }
}