package com.example.kiotsdk.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.wifi.WifiManager
import android.util.Base64
import androidx.appcompat.app.AppCompatActivity
import com.example.kiotsdk.R
import com.kunluiot.sdk.bean.device.DeviceProductTabBean
import java.io.ByteArrayOutputStream

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
     * bitmap转为base64字符串
     * @param context
     * @param resId
     * @return
     */
    fun bitmapToBase64(context: Context, resId: Int): String {
        val bmp = BitmapFactory.decodeResource(context.resources, resId)
        //先将bitmap转为byte[]
        val bitmapOS = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.PNG, 100, bitmapOS)
        val bytes = bitmapOS.toByteArray()
        //将byte[]转为base64
        return Base64.encodeToString(bytes, Base64.DEFAULT)
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

    fun getSceneIcon(): MutableList<Int> {
        val list = mutableListOf<Int>()
        list.add(R.mipmap.ic_scene_1)
        list.add(R.mipmap.ic_scene_2)
        list.add(R.mipmap.ic_scene_3)
        list.add(R.mipmap.ic_scene_4)
        list.add(R.mipmap.ic_scene_5)
        list.add(R.mipmap.ic_scene_6)
        list.add(R.mipmap.ic_scene_7)
        list.add(R.mipmap.ic_scene_8)
        list.add(R.mipmap.ic_scene_9)
        list.add(R.mipmap.ic_scene_10)
        list.add(R.mipmap.ic_scene_11)
        list.add(R.mipmap.ic_scene_12)
        list.add(R.mipmap.ic_scene_13)
        list.add(R.mipmap.ic_scene_14)
        list.add(R.mipmap.ic_scene_15)
        list.add(R.mipmap.ic_scene_16)
        list.add(R.mipmap.ic_scene_17)
        list.add(R.mipmap.ic_scene_18)
        list.add(R.mipmap.ic_scene_19)
        list.add(R.mipmap.ic_scene_20)
        list.add(R.mipmap.ic_scene_21)
        list.add(R.mipmap.ic_scene_22)
        list.add(R.mipmap.ic_scene_23)
        list.add(R.mipmap.ic_scene_24)
        list.add(R.mipmap.ic_scene_25)
        return list
    }
}