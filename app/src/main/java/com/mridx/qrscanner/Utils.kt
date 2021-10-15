package com.mridx.qrscanner

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.mridx.qrscanner.Utils.showDialog
import java.io.FileOutputStream
import java.io.IOException

object Utils {


    fun saveAsPNG(inputBitmap: Bitmap, filePath: String) {

        var out: FileOutputStream? = null
        try {
            out = FileOutputStream(filePath)
            inputBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                out?.close()
            } catch (e: IOException) {
                Log.d("mridx", e.toString())
            }
        }
    }

    fun saveAsJPG(inputBitmap: Bitmap, filePath: String) {
        var out: FileOutputStream? = null
        try {
            out = FileOutputStream(filePath)
            inputBitmap.compress(Bitmap.CompressFormat.JPEG, 75, out)
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                out?.close()
            } catch (e: IOException) {
                Log.d("mridx", e.toString())
            }
        }
    }

    const val POSITIVE_BTN = 1
    const val NEGATIVE_BTN = 2
    fun Context.showDialog(
        title: String,
        message: String,
        positiveBtn: String?,
        negativeBtn: String = "Cancel",
        showNegativeBtn: Boolean = false,
        cancellable: Boolean = false,
        onPressed: ((d: DialogInterface, i: Int) -> Unit)
    ) = run {
        AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(message)
            if (positiveBtn != null)
                setPositiveButton(positiveBtn) { d, _ -> onPressed.invoke(d, POSITIVE_BTN) }
            if (showNegativeBtn)
                setNegativeButton(negativeBtn) { d, _ -> onPressed.invoke(d, NEGATIVE_BTN) }
            setCancelable(cancellable)
        }.create()
    }

    fun Fragment.showDialog(
        title: String,
        message: String,
        positiveBtn: String?,
        negativeBtn: String = "Cancel",
        showNegativeBtn: Boolean = false,
        cancellable: Boolean = false,
        onPressed: ((d: DialogInterface, i: Int) -> Unit)
    ) = run {
        requireContext().showDialog(
            title = title,
            message = message,
            positiveBtn = positiveBtn,
            negativeBtn = negativeBtn,
            showNegativeBtn = showNegativeBtn,
            cancellable = cancellable,
            onPressed = onPressed
        )
    }

    fun Fragment.appSettings() {
        Intent().also { intent ->
            intent.action =
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            intent.data = Uri.fromParts(
                "package",
                BuildConfig.APPLICATION_ID,
                null
            )
            startActivityForResult(
                intent,
                PermissionHandler.APP_SETTINGS_REQUEST
            )
        }
    }

    fun Activity.appSettings() {
        Intent().also { intent ->
            intent.action =
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            intent.data = Uri.fromParts(
                "package",
                BuildConfig.APPLICATION_ID,
                null
            )
            startActivityForResult(
                intent,
                PermissionHandler.APP_SETTINGS_REQUEST
            )
        }
    }

}