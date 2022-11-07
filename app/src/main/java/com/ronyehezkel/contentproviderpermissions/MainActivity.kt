package com.ronyehezkel.contentproviderpermissions

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.ronyehezkel.myhulib.MyLogger


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun getContactsOnClick(view: View) {
        val message = "This is log from application"
        MyLogger.log(message)
    }
}