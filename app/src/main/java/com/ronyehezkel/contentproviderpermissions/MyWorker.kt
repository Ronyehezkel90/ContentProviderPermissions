package com.ronyehezkel.contentproviderpermissions

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.sql.Time

class MyWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result {
        Log.d("lesson15", "Hey Im worker")

        val networkResponse = false
        if(networkResponse){
            return Result.success()
        }
        return Result.retry()
    }

}