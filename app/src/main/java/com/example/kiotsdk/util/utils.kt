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
        // 在nexus 5x 8.1系统上，且应用的target version提升到27，如果关闭了系统安全性和位置信息中隐私设置的位置信息，那么会获取到<unknown ssid>
        // 如果关闭本应用的定位权限，也会同样的情况
        // 但是不同手机的表现不一样
        // 所以当出现unknown ssid 后需要提醒用户去打开设置
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