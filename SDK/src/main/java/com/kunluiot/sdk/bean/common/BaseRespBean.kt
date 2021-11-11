package com.kunluiot.sdk.bean.common


data class BaseRespBean<T>(
    var status: Int = 0,
    var message: String = "",
    var data: T,
    var desc: String = "",
    var code: Int = 0,
    var timestamp: String = ""
)