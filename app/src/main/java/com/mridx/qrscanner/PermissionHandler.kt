package com.mridx.qrscanner

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import com.mridx.qrscanner.Utils.NEGATIVE_BTN
import com.mridx.qrscanner.Utils.POSITIVE_BTN
import com.mridx.qrscanner.Utils.appSettings
import com.mridx.qrscanner.Utils.showDialog

class PermissionHandler {

    companion object {


        const val LOCATION_REQUEST = 600
        const val APP_SETTINGS_REQUEST = 650
        const val CAMERA_REQUEST = 601
        const val STORAGE_REQUEST = 602

        const val basePref = "base_pref"

        const val permissionAskedBefore = "permission_asked_before"
        const val cameraPermissionAskedBefore = "camera_permission_asked_before"
        const val storagePermissionAskedBefore = "storage_permission_asked_before"


        private fun getSharedPerf(context: Context): SharedPreferences {
            return context.applicationContext.getSharedPreferences(basePref, MODE_PRIVATE)
        }

        //region storage permission


        fun checkStorage(context: Context): Boolean {
            return ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }

        fun askStorage(context: Context, backOnCancel: Boolean) {
            askStorage(context, null, backOnCancel)
        }

        fun askStorage(context: Context, fragment: Fragment?, backOnCancel: Boolean) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    context as Activity,
                    Manifest.permission_group.STORAGE
                )
            ) {
                //show rational
                context.showDialog(
                    title = "Permission Required !",
                    message = "Allow Storage Permission to continue",
                    positiveBtn = "Allow",
                    negativeBtn = "Cancel"
                ) { d, i ->
                    d.dismiss()
                    if (i == POSITIVE_BTN) {
                        //allow pressed
                        fragment?.requestPermissions(
                            arrayOf(
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                            ), STORAGE_REQUEST
                        ) ?: ActivityCompat.requestPermissions(
                            context, arrayOf(
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                            ), STORAGE_REQUEST
                        )
                        return@showDialog
                    }
                    if (backOnCancel) context.onBackPressed()
                }.show()
                return
            } else {
                getSharedPerf(context)
                    .getBoolean(storagePermissionAskedBefore, false)
                    .also {
                        if (it) {
                            //show permission needed dialog
                            context.showDialog(
                                title = "Permission Required !",
                                message = "Storage permission is required to create file, capture and process image etc. \nPlease allow it from App Settings to continue",
                                positiveBtn = "Open Settings",
                                negativeBtn = "Cancel"
                            ) { d, i ->
                                d.dismiss()
                                if (i == POSITIVE_BTN) {
                                    //allow pressed
                                    fragment?.appSettings() ?: context.appSettings()
                                    return@showDialog
                                }
                                if (backOnCancel) {
                                    context.onBackPressed()
                                }
                            }.show()
                        } else {
                            fragment?.requestPermissions(
                                arrayOf(
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                                ), STORAGE_REQUEST
                            ) ?: ActivityCompat.requestPermissions(
                                context, arrayOf(
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                                ), STORAGE_REQUEST
                            )
                            getSharedPerf(context).edit {
                                putBoolean(storagePermissionAskedBefore, true)
                            }
                            return@also
                        }
                    }
            }
        }

        //endregion
    }

}