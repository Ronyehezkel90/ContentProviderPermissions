package com.ronyehezkel.contentproviderpermissions

import android.Manifest
import android.Manifest.permission.READ_CONTACTS
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    @SuppressLint("Range")
    private fun getAllContacts() {


        val editText = findViewById<EditText>(R.id.edit_text)
        val nameQuery = editText.text.toString()
        val selection = "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} LIKE ?"
        val selectionArgs = arrayOf("%$nameQuery%")
        val phonesCursor = contentResolver.query(
            CONTENT_URI,
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
        val isPermissionAlreadyGranted = ContextCompat.checkSelfPermission(this, READ_CONTACTS)
        if (isPermissionAlreadyGranted == PackageManager.PERMISSION_GRANTED) {
            getAllContacts()
        } else {
            val needToExplainThePermission =
                ActivityCompat.shouldShowRequestPermissionRationale(this, READ_CONTACTS)
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