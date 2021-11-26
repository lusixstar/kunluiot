package com.example.kiotsdk.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.wifi.WifiManager
import android.util.Base64
import androidx.appcompat.app.AppCompatActivity
import com.kunluiot.sdk.bean.device.DeviceProductTabBean

object DemoUtils {

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

    /**
     * 将Base64字符串形式存储的图片转成Bitmap
     */
    fun base64CodeToBitmap(base64Code: String): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            val bitmapArray = Base64.decode(base64Code, Base64.DEFAULT)
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.size)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return bitmap
    }
}