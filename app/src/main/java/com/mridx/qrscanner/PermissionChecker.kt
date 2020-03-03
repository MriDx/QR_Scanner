package com.mridx.qrscanner

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat

public class PermissionChecker(mainActivity: MainActivity) {

    private val context = mainActivity

    fun showToast(text: String) {
        Log.d("mm", text)
        Toast.makeText(context, "Show Toast", Toast.LENGTH_SHORT).show()
    }

    fun checkPermission(): Boolean = ActivityCompat.checkSelfPermission(
        context,
        Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED


    fun askForPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                context,
                arrayOf(Manifest.permission.CAMERA).toString()
            )
        ) {

            Toast.makeText(context, "Allow permission from settings", Toast.LENGTH_SHORT).show()
            return

        }
        ActivityCompat.requestPermissions(context, arrayOf(Manifest.permission.CAMERA), 200)
    }


}