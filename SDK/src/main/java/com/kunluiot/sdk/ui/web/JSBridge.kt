package com.kunluiot.sdk.ui.web

import android.webkit.JavascriptInterface
import com.elvishew.xlog.XLog
import com.kunluiot.sdk.bean.common.WebBridgeBean
import com.kunluiot.sdk.callback.common.OnWebControlSyncCall
import com.kunluiot.sdk.callback.common.OnWebControlVoidCall
import com.kunluiot.sdk.util.JsonUtils

class JSBridge {

    private var sync: OnWebControlSyncCall? = null
    private var void: OnWebControlVoidCall? = null

    fun setCallback(sync: OnWebControlSyncCall, void: OnWebControlVoidCall) {
        this.sync = sync
        this.void = void
    }

    @JavascriptInterface
    fun __syncCall(message: String): String {
//        XLog.e("__syncCall---->$message")
        val bean: WebBridgeBean = JsonUtils.fromJson(message, WebBridgeBean::class.java)
        return sync?.call(bean) ?: ""
    }

    @JavascriptInterface
    fun __voidCall(message: String) {
//        XLog.e("__voidCall---->$message")
        val bean: WebBridgeBean = JsonUtils.fromJson(message, WebBridgeBean::class.java)
        void?.call(bean)
    }
}