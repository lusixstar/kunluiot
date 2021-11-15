package com.example.kiotsdk.bean

data class ConfigWifiBean(
    val action: String = "",
    val params: ConfigWifiParamsBean = ConfigWifiParamsBean(),
    val ip: String = "",
)

data class ConfigWifiParamsBean(
    val devTid: String = "",
    val STEP: Int = 0,
    val encodeConfigType: Int = 0,
    val getIp: String = "",
    val PIN: String = "",
    val bind: Int = 0,
    val code: Int = 0,
    val DNSTimer: Int = 0,
    val DNSType: Int = 0,
    val binVer: String = "",
)