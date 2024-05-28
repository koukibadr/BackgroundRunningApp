package com.bk.backgroundrunnningapp

interface MessageListenerInterface {
    fun messageReceived(message: String): String
}