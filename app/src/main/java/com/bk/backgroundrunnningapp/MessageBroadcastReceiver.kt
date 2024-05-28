package com.bk.backgroundrunnningapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony


class MessageBroadcastReceiver: BroadcastReceiver() {

    companion object {
        lateinit var mListener : MessageListenerInterface
        fun bindListener(listener: MessageListenerInterface) {
            mListener = listener
        }
    }
    override fun onReceive(context: Context?, intent: Intent?) {
        for (smsMessage in Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
            val messageBody = smsMessage.messageBody
            mListener.messageReceived(messageBody)
        }
    }


}