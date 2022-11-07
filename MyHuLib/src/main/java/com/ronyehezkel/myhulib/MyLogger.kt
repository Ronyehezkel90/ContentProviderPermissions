package com.ronyehezkel.myhulib

import android.util.Log
import retrofit2.Retrofit

object MyLogger {
    fun log(log:String){
        Log.d("MyLogger", log)
        Retrofit.Builder().build()
    }
}