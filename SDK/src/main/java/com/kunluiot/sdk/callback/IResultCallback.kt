package com.kunluiot.sdk.callback

interface IResultCallback {
    fun onError(code: String, error: String)
    fun onSuccess()
}

interface IResultStringCallback {
    fun onError(code: String, error: String)
    fun onSuccess(str: String)
}