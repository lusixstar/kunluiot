package com.kunluiot.sdk.request

import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.thirdlib.kalle.Headers
import com.kunluiot.sdk.util.FileUtil
import com.kunluiot.sdk.util.HMACSHA256
import java.util.*

internal object KunLuHelper {

    const val CACHE_DIR_NAME = "kunlu"
    const val CACHE_URL_NAME = "url"
    const val CACHE_INDEX_HTML = "index.html"
    const val CACHE_ZIP = "pck.zip"

    const val LOCAL_FILE_PRE = "file://"

    val INJECT_JS = FileUtil.getStringFromAssets(KunLuHomeSdk.instance.getApp(), "inject.js")

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

    fun getUuid(): String {
        val uuid: String = UUID.randomUUID().toString()
        return uuid.replace("-".toRegex(), "")
    }
}