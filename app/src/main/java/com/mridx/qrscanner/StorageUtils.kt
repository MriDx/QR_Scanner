package com.mridx.qrscanner

import android.widget.Toast

import android.R
import android.content.Context
import java.io.*


object StorageUtils {

    // ----------------------------------
    // READ & WRITE ON STORAGE
    // ----------------------------------


    private fun writeOnFile(text: String, file: File): Boolean {
        try {
            file.parentFile.mkdirs()
            val fos = FileOutputStream(file)
            val w: Writer = BufferedWriter(OutputStreamWriter(fos))
            w.use { w ->
                w.write(text)
                w.flush()
                fos.fd.sync()
                return true
            }
        } catch (e: IOException) {
            return false
        }
    }

}