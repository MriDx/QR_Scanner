package com.mridx.qrscanner

import android.util.Base64
import java.io.UnsupportedEncodingException

class Base64Helper() {


    fun decode2(string: String): String? {
        var data: ByteArray? = ByteArray(0)
        try {
            data = Base64.decode(
                string.toByteArray(charset("UTF-8")),
                Base64.DEFAULT
            )
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return String(data!!)
    }

    fun decode(qrData: String): String? {
        var data: ByteArray? = ByteArray(0)
        try {
            data = Base64.decode(qrData.toByteArray(charset("UTF-8")), Base64.DEFAULT)
        } catch (e: java.lang.IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
            return decodeURLSAFE(qrData)
        }
        return String(data!!)
    }

    private fun  decodeURLSAFE(qrData: String) : String? {
        var data: ByteArray? = ByteArray(0)
        try {
            data = Base64.decode(qrData.toByteArray(charset("UTF-8")), Base64.URL_SAFE)
        } catch (e: java.lang.IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()

        }
        return String(data!!)
    }

}