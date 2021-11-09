package com.example.kiotsdk.app

import android.app.Application
import com.kunluiot.sdk.KunLuHomeSdk

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        KunLuHomeSdk.instance.init(this)
    }
}