package com.mridx.qrscanner

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import kotlinx.android.synthetic.main.result_viewer.*

class ResultViewer : AppCompatActivity() {

    var result = ""

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.result_viewer)

        appVersion.text = "v " + BuildConfig.VERSION_NAME

        result = intent.extras?.get("RESULT").toString()
        resultTextView.text = result

        openURL.setOnClickListener { openBrowser() }
        decodeResult.setOnClickListener { decodeQrResult() }
        shareResult.setOnClickListener { shareQrResult() }
        copyResult.setOnClickListener { copyResult() }

    }

    private fun shareQrResult() {
        ShareCompat.IntentBuilder.from(this)
            .setType("text/plain")
            .setChooserTitle("Share Qr scanned data")
            .setText("$result\nScanned with QR Scanner")
            .startChooser()
    }

    private fun decodeQrResult() {
        val decoded = Base64Helper().decode(result)
        decodedResultView.text = decoded
        decodedViewer.visibility = View.VISIBLE
    }

    private fun openBrowser() {
        if (result.startsWith("http") || result.startsWith("www")) {
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            intent.data = Uri.parse(result)
            startActivity(intent)
        } else{
            Toast.makeText(this, "not a valid url", Toast.LENGTH_SHORT).show()
        }
    }

    private fun copyResult() {
        val clipBoard = this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("Qr Scanner result", result)
        clipBoard.setPrimaryClip(clipData)
        Toast.makeText(this, "Qr result copied", Toast.LENGTH_SHORT).show()
    }
}