package com.mridx.qrscanner

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.appVersion

class MainActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        appVersion.text = "v " + BuildConfig.VERSION_NAME

        scannerCard.setOnClickListener { startScanner() }

    }

    private fun startScanner() {
        if (PermissionChecker(this).checkPermission()) {
            Log.d("mridx", "start scanner")
            IntentIntegrator(this).setOrientationLocked(true).setPrompt("Scan QR").initiateScan()
        } else {
            PermissionChecker(this).askForPermission()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 200) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startScanner()
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && result != null) {
            //Toast.makeText(this, result.contents, Toast.LENGTH_LONG).show()
            parseResult(result.contents)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    public fun parseResult(result: String) {
        val intent = Intent(this, ResultViewer::class.java)
        intent.putExtra("RESULT", result)
        startActivity(intent)
    }

}
