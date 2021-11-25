package com.kunluiot.sdk.ui.web

import android.webkit.JavascriptInterface
import com.kunluiot.sdk.bean.common.WebBridgeBean
import com.kunluiot.sdk.callback.common.OnWebControlSyncCall
import com.kunluiot.sdk.callback.common.OnWebControlVoidCall
import com.kunluiot.sdk.util.JsonUtils
import com.kunluiot.sdk.util.log.KunLuLog

class JSBridge {

    private var sync: OnWebControlSyncCall? = null
    private var void: OnWebControlVoidCall? = null

    fun setCallback(sync: OnWebControlSyncCall, void: OnWebControlVoidCall) {
        this.sync = sync
        this.void = void
    }

    @JavascriptInterface
    fun __syncCall(message: String): String {
        KunLuLog.e("__syncCall---->$message")
        val bean: WebBridgeBean = JsonUtils.fromJson(message, WebBridgeBean::class.java)
        return sync?.call(bean) ?: ""
    }

    @JavascriptInterface
    fun __voidCall(message: String) {
        KunLuLog.e("__voidCall---->$message")
        val bean: WebBridgeBean = JsonUtils.fromJson(message, WebBridgeBean::class.java)
        void?.call(bean)
    }
}