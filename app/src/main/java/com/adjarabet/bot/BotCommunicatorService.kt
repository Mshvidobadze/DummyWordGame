package com.adjarabet.bot

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder

class BotCommunicatorService : Service() {

    val localBinder = LocalBinder()

    override fun onBind(intent: Intent): IBinder {
        return localBinder
    }

    inner class LocalBinder: Binder(){

        fun getInstance(): BotCommunicatorService{
            return this@BotCommunicatorService
        }
    }

    fun getBotAnswer(): String{

        val response: String
        val rand = (0..100).random()
        response = if(rand > 97){
            "TOO_MUCH_FOR_ME"
        }else{
            generateString()
//            response = List(3){(('a'..'z')+(('A'..'Z'))).random()}.joinToString {""}
        }
        return response
    }

    private fun generateString(): String{
        val characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
        val stringB = StringBuilder(3)
        for (x in 0 until 3){
            val random = (characters.indices).random()
            stringB.append(characters[random])
        }
        return stringB.toString()
    }



}