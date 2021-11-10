package com.kunluiot.sdk.helper

import android.text.TextUtils
import com.kunluiot.sdk.kalle.Headers
import com.kunluiot.sdk.util.HMACSHA256
import java.util.*

object KunLuHelper {

    private const val lang = "cn"
    private const val accessId = "db96ba0828a146498165f4de8b37dca0"
    private const val accessSecret = "gCijFSQYavXhfLjsk2,L86u@VMWmxXFcBAWytvBAarkfwvaxLp4uYuK/Fbb02dz;ZRCMcByt,QH8RA7vR;3Dzuih6tMkaka3EEwgWA@kv3nHPN5GLK3jvKg8"

    fun getSign(): Headers {
        val map = Headers()

        val nonce = getUuid()
        map.set("nonce", nonce)

        map.set("lang", lang)

        map.set("accessId", accessId)

        val time = getTimeMillis()
        map.set("t", time.toString())

        val sign = "$accessId$lang$nonce$time"
        val result = HMACSHA256.sha256_HMAC(sign, accessSecret)
        map.set("sign", result)

        return map
    }

    private fun getTimeMillis(): Long {
        return System.currentTimeMillis()
    }

    private fun getUuid(): String {
        val uuid: String = UUID.randomUUID().toString()
        return uuid.replace("-".toRegex(), "")
    }

    fun isEmail(str: String): Boolean {
        return if (TextUtils.isEmpty(str)) {
            false
        } else {
            str.contains("@") && str.contains(".")
        }
    }
}