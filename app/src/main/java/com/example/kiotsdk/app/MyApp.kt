package com.example.kiotsdk.app

import android.app.Application
import com.kunluiot.sdk.KunLuHomeSdk

class MyApp : Application() {

    private val accessId = "db96ba0828a146498165f4de8b37dca0"
    private val accessSecret = "gCijFSQYavXhfLjsk2,L86u@VMWmxXFcBAWytvBAarkfwvaxLp4uYuK/Fbb02dz;ZRCMcByt,QH8RA7vR;3Dzuih6tMkaka3EEwgWA@kv3nHPN5GLK3jvKg8"


    override fun onCreate() {
        super.onCreate()
        KunLuHomeSdk.instance.init(this, accessId, accessSecret)
    }
}