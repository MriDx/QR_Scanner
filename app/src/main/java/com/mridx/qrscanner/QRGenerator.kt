package com.mridx.qrscanner

import android.R.attr
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.WriterException

import android.graphics.Bitmap
import android.media.tv.TvContract.Programs.Genres.encode
import android.os.Environment
import android.os.UserManager
import android.util.Base64.encode
import android.widget.Toast
import androidx.core.app.ShareCompat
import androidx.core.net.toUri
import androidx.core.os.EnvironmentCompat
import androidx.core.os.UserManagerCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged

import com.journeyapps.barcodescanner.BarcodeEncoder

import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter

import com.google.zxing.common.BitMatrix
import kotlinx.android.synthetic.main.qr_generated_view.*
import java.io.File
import java.net.URLConnection
import java.util.*
import android.R.attr.path
import android.net.Uri
import androidx.core.content.FileProvider


class QRGenerator : AppCompatActivity() {


    private var _input = ""
    private var generatedQR: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.qr_generated_view)

        backBtn.setOnClickListener { onBackPressed() }
        inputField.doAfterTextChanged {
            _input = it.toString()
        }

        qrGenerateBtn.setOnClickListener {
            startQrGeneration()
        }

        saveQR.setOnClickListener {
            proceedSaveQR()
        }

        shareResult.setOnClickListener {
            shareQR()
        }

    }

    private fun shareQR() {
        generatedQR ?: returnWithToast("No QR Generated to share !") ?: return
        val path = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val parent = File(path, "shareable")
        if (parent.exists()) {
            parent.deleteRecursively()
        }
        parent.mkdirs()
        val savePath = File(parent, "QR_${Date().time}.png")
        Utils.saveAsPNG(generatedQR!!, savePath.path)
        /*ShareCompat.IntentBuilder.from(this)
            .setStream(savePath.toUri())
            .setType(URLConnection.guessContentTypeFromName(savePath.name))
            .startChooser()*/
        val uri = getUriFromFiles(savePath)
            ?: return
        startShareIntent(uri)
    }

    private fun getUriFromFiles(file: File): Uri? {
        return try {
            FileProvider.getUriForFile(this, "${BuildConfig.APPLICATION_ID}.FileProvider", file)
        } catch (e: Exception) {
            e.printStackTrace()
            returnWithToast("Something went wrong ! Try saving the qr and share manually.")
            null
        }

    }

    private fun startShareIntent(uri: Uri) {
        val shareToneIntent = Intent(Intent.ACTION_SEND)
        shareToneIntent.putExtra(Intent.EXTRA_STREAM, uri)
        shareToneIntent.type = "image/png"
        startActivity(shareToneIntent)
    }

    private fun proceedSaveQR() {
        if (!checkStoragePermission()) return
        generatedQR ?: returnWithToast("No QR Generated to save !") ?: return
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val toSave = File(path, "QR_${Date().time}.png")
        Utils.saveAsPNG(generatedQR!!, toSave.path)
        Toast.makeText(this, "QR saved successfully at Pictures Directory !", Toast.LENGTH_SHORT)
            .show()
    }

    private fun returnWithToast(s: String): Any? {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
        return null
    }

    private fun checkStoragePermission(): Boolean {
        return if (!PermissionHandler.checkStorage(this)) {
            PermissionHandler.askStorage(this, false)
            false
        } else
            true
    }


    private fun startQrGeneration() {
        generatedQR = generate(input = _input) ?: return
        qrGeneratedResultView.apply {
            setImageBitmap(generatedQR)
        }.isVisible = true
    }


    private fun generate(input: String): Bitmap? {
        return try {
            val bitMatrix: BitMatrix =
                MultiFormatWriter().encode(input, BarcodeFormat.QR_CODE, 1080, 1080)
            val barcodeEncoder = BarcodeEncoder()
            return barcodeEncoder.createBitmap(bitMatrix)
        } catch (e: WriterException) {
            e.printStackTrace()
            Toast.makeText(this, "Failed to generate QR", Toast.LENGTH_SHORT).show()
            null
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PermissionHandler.STORAGE_REQUEST) {
            proceedSaveQR()
            return
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PermissionHandler.APP_SETTINGS_REQUEST) {
            proceedSaveQR()
            return
        }
    }

}