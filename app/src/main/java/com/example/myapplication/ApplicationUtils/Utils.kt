package com.example.myapplication

import android.content.Context
import android.util.Log
import android.widget.Toast

fun Any.logd(message: Any? = "no message!") {
  Log.d("Main", message.toString())
}

fun Context.toast(message: CharSequence) =
  Toast.makeText(this, message, Toast.LENGTH_LONG).show()