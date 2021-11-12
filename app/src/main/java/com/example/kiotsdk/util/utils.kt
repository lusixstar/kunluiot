package com.example.kiotsdk.util

import android.text.TextUtils
import com.kunluiot.sdk.bean.device.DeviceProductTabBean

object DemoUtils {

    fun isEmail(str: String): Boolean {
        return if (TextUtils.isEmpty(str)) {
            false
        } else {
            str.contains("@") && str.contains(".")
        }
    }

    fun getSingle(list: MutableList<DeviceProductTabBean>): MutableList<DeviceProductTabBean> {
        val tempList: MutableList<DeviceProductTabBean> = mutableListOf()
        val it: Iterator<DeviceProductTabBean> = list.iterator()
        while (it.hasNext()) {
            val obj = it.next()
            if (!tempList.contains(obj)) {
                tempList.add(obj)
            }
        }
        return tempList
    }
}