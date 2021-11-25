package com.kunluiot.sdk.util.log

import android.util.Log
import com.kunluiot.sdk.BuildConfig

object KunLuLog {

    //是否需要打印bug
    var DEBUG = BuildConfig.DEBUG
    private const val TAG = "KunLuSDK"

    // 下面四个是默认tag的函数
    fun i(msg: String?) {
        if (DEBUG) Log.i(TAG, msg!!)
    }

    fun d(msg: String?) {
        if (DEBUG) Log.d(TAG, msg!!)
    }

    fun e(msg: String?) {
        if (DEBUG) Log.e(TAG, msg!!)
    }

    fun v(msg: String?) {
        if (DEBUG) Log.v(TAG, msg!!)
    }

    // 下面是传入自定义tag的函数
    fun i(tag: String?, msg: String?) {
        if (DEBUG) Log.i(tag, msg!!)
    }

    fun d(tag: String?, msg: String?) {
        if (DEBUG) Log.d(tag, msg!!)
    }

    fun e(tag: String?, msg: String?) {
        if (DEBUG) Log.e(tag, msg!!)
    }

    fun v(tag: String?, msg: String?) {
        if (DEBUG) Log.v(tag, msg!!)
    }

    fun largeI(tagName: String?, message: String) {
        var msg = message
        if (DEBUG) {
            val segmentSize = 3 * 1024
            val length = msg.length.toLong()
            if (length <= segmentSize) { // 长度小于等于限制直接打印
                Log.i(tagName, msg)
            } else {
                while (msg.length > segmentSize) { // 循环分段打印日志
                    val logContent = msg.substring(0, segmentSize)
                    msg = msg.replace(logContent, "")
                    Log.i(tagName, logContent)
                }
                Log.i(tagName, msg) // 打印剩余日志
            }
        }
    }
}