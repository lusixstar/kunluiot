package com.example.kiotsdk.util

import android.text.TextUtils

object DemoUtils {

    fun isEmail(str: String): Boolean {
        return if (TextUtils.isEmpty(str)) {
            false
        } else {
            str.contains("@") && str.contains(".")
        }
    }
}