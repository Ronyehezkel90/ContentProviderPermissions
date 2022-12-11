package com.ronyehezkel.contentproviderpermissions

import android.content.Intent.ACTION_BATTERY_CHANGED
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.work.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    val broadcastReceiver = MyBroadcastReceiver()
    val workManager = WorkManager.getInstance(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        registerOnClick()
    }


    private fun registerOnClick() {
        var intentFilter = IntentFilter(ACTION_BATTERY_CHANGED)
        ContextCompat.registerReceiver(
            applicationContext,
            broadcastReceiver,
            intentFilter,
            ContextCompat.RECEIVER_EXPORTED
        )
        intentFilter = IntentFilter("MyBroadcastEvent")
        ContextCompat.registerReceiver(
            applicationContext,
            broadcastReceiver,
            intentFilter,
            ContextCompat.RECEIVER_EXPORTED
        )
    }

    fun workerOnClick(view: View) {
        periodicWorkRequest()
    }

    private fun periodicWorkRequest() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val myWork =
            PeriodicWorkRequestBuilder<MyWorker>(15, TimeUnit.MINUTES, 10, TimeUnit.MINUTES)
//            .setInitialDelay(30, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .setBackoffCriteria(
                    BackoffPolicy.EXPONENTIAL,
                    10,
                    TimeUnit.MINUTES
                )
                .build()
        workManager.enqueue(myWork)
    }

    private fun oneTimeWork() {
        val myWork = OneTimeWorkRequestBuilder<MyWorker>()
            .setInitialDelay(10, TimeUnit.SECONDS)
            .build()
        workManager.enqueue(myWork)
    }
}