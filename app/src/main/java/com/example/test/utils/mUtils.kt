package com.example.test.utils

import android.content.Context
import android.util.Log
import android.widget.Toast

object mUtils {

    fun mLog(message: String) {
        Log.i("SANJAY", "mLog: $message")
    }

    fun mToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}