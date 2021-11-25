package com.example.kiotsdk.util

import android.content.Context
import android.net.wifi.WifiManager
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import com.kunluiot.sdk.bean.device.DeviceProductTabBean

object DemoUtils {

    fun isEmail(str: String): Boolean {
        return if (TextUtils.isEmpty(str)) {
            false
        } else {
            str.contains("@") && str.contains(".")
        }
    }

    fun getTabSingle(list: List<DeviceProductTabBean>): MutableList<DeviceProductTabBean> {
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

    fun getCurrentSSID(context: Context): String {
        val wifiManager = context.applicationContext.getSystemService(AppCompatActivity.WIFI_SERVICE) as WifiManager
        var currentWifi = ""
        if (wifiManager != null) {
            val wifiInfo = wifiManager.connectionInfo
            if (wifiInfo != null) {
                val ssid = wifiInfo.ssid
                if (ssid != null) {
                    currentWifi = ssid.replace("\"", "")
                }
            }
        }
        return currentWifi
    }
}