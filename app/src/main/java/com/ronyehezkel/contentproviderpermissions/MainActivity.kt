package com.ronyehezkel.contentproviderpermissions

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent.ACTION_BATTERY_CHANGED
import android.content.IntentFilter
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.work.*
import com.google.android.material.snackbar.Snackbar
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

    @SuppressLint("Range")
    private fun getAllContacts() {


        val editText = findViewById<EditText>(R.id.edit_text)
//        val nameQuery = editText.text.toString()
        val nameQuery = "rent"
        val selection = "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} LIKE ?"
        val selectionArgs = arrayOf("%$nameQuery%")
        val phonesCursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            selection,
            selectionArgs,
            null
        )
        while (phonesCursor!!.moveToNext()) {
            val name =
                phonesCursor.getString(phonesCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            Log.d("lesson", name)
        }


    }

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Log.d("Permissions", "Granted")
                getAllContacts()
            } else {
                Log.d("Permissions", "Not Granted")
            }
        }

    private fun validatePermissionAndGetContacts() {
        val isPermissionAlreadyGranted = ContextCompat.checkSelfPermission(this,
            Manifest.permission.READ_CONTACTS
        )
        if (isPermissionAlreadyGranted == PackageManager.PERMISSION_GRANTED) {
            getAllContacts()
        } else {
            val needToExplainThePermission =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS
                )
            if (needToExplainThePermission) {
                Snackbar.make(
                    (findViewById<View>(R.id.button).rootView),
                    "Hey permission is required in order to find the contacts in your contacts list.",
                    Snackbar.LENGTH_LONG
                ).setAction("ASK") {
                    requestPermission.launch(Manifest.permission.READ_CONTACTS)
                }.show()
            } else {
                requestPermission.launch(Manifest.permission.READ_CONTACTS)
            }
        }
    }

    fun getContactsOnClick(view: View) {
        validatePermissionAndGetContacts()
//        getAllContacts()
    }
}