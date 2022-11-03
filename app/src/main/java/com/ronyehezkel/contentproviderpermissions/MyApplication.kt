package com.ronyehezkel.contentproviderpermissions

import android.app.Application
import android.util.Log

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
//        applicationContext
    }

    override fun onTerminate() {
        Log.d("lesson_15", "terminate app")
        super.onTerminate()
    }
}