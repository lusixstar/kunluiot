package com.kunluiot.sdk.util

import android.text.TextUtils
import java.lang.Exception

object ValidatorUtil {

    fun isEmail(str: String): Boolean {
        return if (TextUtils.isEmpty(str)) {
            false
        } else {
            str.contains("@") && str.contains(".")
        }
    }
}