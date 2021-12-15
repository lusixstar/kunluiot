package com.kunluiot.sdk.ui.web

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.elvishew.xlog.XLog
import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.R
import com.kunluiot.sdk.api.device.KunLuDeviceType
import com.kunluiot.sdk.bean.common.BaseSocketBean
import com.kunluiot.sdk.bean.common.WebBridgeBean
import com.kunluiot.sdk.bean.common.WebBridgeParametersBean
import com.kunluiot.sdk.bean.device.DeviceNewBean
import com.kunluiot.sdk.bean.device.DeviceOperationBean
import com.kunluiot.sdk.bean.device.DeviceOperationProtocolBean
import com.kunluiot.sdk.request.DeviceApi
import com.kunluiot.sdk.request.KunLuHelper
import com.kunluiot.sdk.request.ReqApi
import com.kunluiot.sdk.thirdlib.ws.websocket.SocketListener
import com.kunluiot.sdk.thirdlib.ws.websocket.response.ErrorResponse
import com.kunluiot.sdk.util.CacheUtils
import com.kunluiot.sdk.util.JsonUtils
import com.kunluiot.sdk.util.MD5Util
import com.kunluiot.sdk.util.Tools
import org.java_websocket.framing.Framedata
import java.io.File
import java.nio.ByteBuffer
import java.util.*

class DeviceWebControlActivity : AppCompatActivity() {

    private lateinit var mWebView: WebView

    private var mUrl = ""
    private var mBean = DeviceNewBean()

    private var mProtocolBean = DeviceOperationBean()
    private var mProtocolMap: Map<String, DeviceOperationProtocolBean> = mutableMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_control)

        mWebView = findViewById(R.id.web)

        getIntentData()
        KunLuHomeSdk.instance.getWebSocketManager()?.addListener(webSocketListener)

    }

    private val webSocketListener = object : SocketListener {
        override fun onConnected() {}
        override fun onConnectFailed(e: Throwable?) {}
        override fun onDisconnect() {}
        override fun onSendDataError(errorResponse: ErrorResponse?) {}

        override fun <T : Any?> onMessage(message: String?, data: T) {
            message?.let { getSocketMsg(message, JsonUtils.toJson(data)) }
        }

        override fun <T : Any?> onMessage(bytes: ByteBuffer?, data: T) {}
        override fun onPing(framedata: Framedata?) {}
        override fun onPong(framedata: Framedata?) {}
    }

    private fun getSocketMsg(message: String, toJson: String) {
        val msg: BaseSocketBean = JsonUtils.fromJson(toJson, BaseSocketBean::class.java)
        XLog.e("msg == $msg")
    }

    override fun onDestroy() {
        super.onDestroy()
        KunLuHomeSdk.instance.getWebSocketManager()?.removeListener(webSocketListener)
    }

    private fun getJSBridgeBean(bean: WebBridgeBean): String {
        if (bean.name.isNotEmpty()) {
            val allMap: MutableMap<String, Any> = HashMap()
            when (bean.name) {
                "getDomain" -> {
                    val domain = ReqApi.KHA_DOMAIN
                    val domainMap: MutableMap<String, Any?> = HashMap()
                    domainMap["domain"] = domain
                    domainMap["user"] = ReqApi.KHA_WEB_BASE_URL.replace(ReqApi.KHA_DOMAIN, domain)
                    domainMap["test"] = false
                    domainMap["uaa"] = ReqApi.KHA_UAA_BASE_URL.replace(ReqApi.KHA_DOMAIN, domain)
                    domainMap["console"] = ReqApi.KHA_CONSOLE_BASE_URL.replace(ReqApi.KHA_DOMAIN, domain)
//                    domainMap["pid"] = "00000000000"
                    allMap["result"] = domainMap
                }
                "close" -> {
                    finish()
                }
                "user" -> {
                    val userMap: MutableMap<String, Any> = HashMap()
                    userMap["token"] = KunLuHomeSdk.instance.getSessionBean()?.accessToken ?: ""
                    userMap["access_token"] = KunLuHomeSdk.instance.getSessionBean()?.accessToken ?: ""
                    userMap["appTid"] = Tools.getAppTid() + "web"
                    userMap["lang"] = Tools.getLanguage()
                    userMap["uid"] = KunLuHomeSdk.instance.getSessionBean()?.user ?: ""
                    allMap["result"] = userMap
                }
                "getMsgId" -> {
                    allMap["result"] = KunLuHomeSdk.instance.getMsgId().toString()
                }
                "getNetwork" -> {
                    val networkMap: MutableMap<String, Any> = HashMap()
                    networkMap["network"] = "WiFi"
                    allMap["result"] = networkMap
                }
                "getPlatform" -> {
                    val platform: MutableMap<String, Any> = HashMap()
                    platform["platform"] = "Android"
                    allMap["result"] = platform
                }
                "getDevice" -> {
                    mBean.androidPageZipURL
                    if (mBean.virtual) {
                        mBean.online = true
                    }
                    if (mBean.devType == KunLuDeviceType.DEVICE_SUB) {
                        mBean.subDevTid = mBean.devTid
                        mBean.devTid = mBean.parentDevTid
                        mBean.ctrlKey = mBean.parentCtrlKey
                    }
                    allMap["result"] = mBean
                }
                "getProtocol" -> {
                    allMap["result"] = mProtocolMap
                }
                "getControl" -> {
                    val controlMap: MutableMap<String, Any> = HashMap()
                    controlMap["mode"] = "Cloud"
                    allMap["result"] = controlMap
                }
                "open" -> {
                    val parametersBean: WebBridgeParametersBean = JsonUtils.fromJson(bean.parameters.toString(), WebBridgeParametersBean::class.java)
                    if (parametersBean.url.isNotEmpty()) {
                        val i = Intent(Intent.ACTION_VIEW)
                        i.data = Uri.parse(parametersBean.url)
                        startActivity(i)
                    }
                }
                "errorLog" -> {
                    // 打印错误日志
                    val parametersBean: WebBridgeParametersBean = JsonUtils.fromJson(bean.parameters.toString(), WebBridgeParametersBean::class.java)
                    XLog.e("errorLog::  $mUrl  $parametersBean")
                }
                "send" -> {
                    val params = bean.parameters as Map<*, *>
                    val command = params["command"] as Map<*, *>

                    val commandParams = command["params"] as Map<*, *>?
                    val rawMap = (commandParams!!["data"] as Map<*, *>?)?.toMutableMap()

                    if (mBean.virtual) {
                        if (mBean.workModeType == "JSON_TRANSPARENT" || mBean.workModeType == "JSON_CTRL") {
                            val resp: MutableMap<String, Any> = HashMap()
                            resp["msgId"] = KunLuHomeSdk.instance.getMsgId()
                            resp["action"] = "devSend"
                            rawMap?.set("cmdId", 1)
                            resp["params"] = commandParams
                            val header = ("javascript:document.dispatchEvent(new CustomEvent('notifyDevEvent', {detail:" + JsonUtils.toJson(resp)) + "}));"

                            runOnUiThread { mWebView.loadUrl(header) }
                        }
                    }
                    sendWebSocketText(JsonUtils.toJson(command))
                }
            }
            return JsonUtils.toJson(allMap)
        }
        return ""
    }

    private fun sendWebSocketText(text: String) {
        KunLuHomeSdk.instance.getMsgId()
        KunLuHomeSdk.instance.getWebSocketManager()?.send(text)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        val jsBridge = JSBridge()
        jsBridge.setCallback({ getJSBridgeBean(it) }, { getJSBridgeBean(it) })
        mWebView.addJavascriptInterface(jsBridge, "__HEKR__BRIDGE")
        val webSettings = mWebView.settings
        webSettings.domStorageEnabled = true
        webSettings.javaScriptEnabled = true
        webSettings.allowUniversalAccessFromFileURLs = true
        webSettings.builtInZoomControls = true
        webSettings.displayZoomControls = false
        webSettings.domStorageEnabled = true
        webSettings.useWideViewPort = false
        webSettings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        WebView.setWebContentsDebuggingEnabled(true)
        mWebView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                view?.evaluateJavascript(KunLuHelper.INJECT_JS) { }
            }
        }
        mWebView.loadUrl(mUrl)
    }

    private fun getZipUrl() {
        if (mBean.androidPageZipURL.isNotEmpty()) {
//            val cacheDir = KunLuHomeSdk.instance.getApp().cacheDir.absolutePath + File.separator + KunLuHelper.CACHE_DIR_NAME + File.separator + KunLuHelper.CACHE_URL_NAME + File.separator + MD5Util.md5(mBean.androidPageZipURL)
            val cacheDir = KunLuHomeSdk.instance.getApp().externalCacheDir?.absolutePath + File.separator + KunLuHelper.CACHE_DIR_NAME + File.separator + KunLuHelper.CACHE_URL_NAME + File.separator + MD5Util.md5(mBean.androidPageZipURL)
            val indexHtml = cacheDir + File.separator + KunLuHelper.CACHE_INDEX_HTML
            val isOk = isFileExist(indexHtml)
            if (!isOk) {
                KunLuHomeSdk.commonImpl.downloadsUrlFile(mBean.androidPageZipURL, { code, msg -> XLog.e("code == $code, msg == $msg") }, {
                    XLog.e("it == $it")
                    mUrl = KunLuHelper.LOCAL_FILE_PRE + it
                    initWebView()
                })
            } else {
                mUrl = KunLuHelper.LOCAL_FILE_PRE + indexHtml
                XLog.e("indexHtml == $indexHtml")
                initWebView()
            }
        }
    }

    private fun getIntentData() {
        intent?.let {
            mBean = it.getParcelableExtra(BEAN) ?: DeviceNewBean()
        }
        if (mBean.productPublicKey.isNotEmpty()) {
            val url: String = ReqApi.KHA_CONSOLE_BASE_URL + DeviceApi.KHA_API_DEVICE_OPERATION_LIST + "/" + Tools.getProtocolCacheId(mBean)
            val cache: String = CacheUtils.getCache(url)
            if (cache.isNotEmpty()) {
                val stateResult: DeviceOperationBean = JsonUtils.fromJson(cache, DeviceOperationBean::class.java)
                mProtocolBean = stateResult
                mProtocolMap = stateResult.protocol
                getZipUrl()
            }
            KunLuHomeSdk.deviceImpl.getDeviceOperationList(mBean.productPublicKey, { code, msg -> XLog.e("code == $code, msg == $msg") }, {
                mProtocolBean = it
                mProtocolMap = it.protocol
                val json = JsonUtils.toJson(mProtocolBean)
                if (cache != json) {
                    CacheUtils.setCache(url, json)
                    getZipUrl()
                }
            })
        }
    }

    override fun onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack()
        } else super.onBackPressed()
    }

    private fun isFileExist(path: String): Boolean {
        val htmlFile = File(path)
        if (htmlFile.exists()) {
            return true
        }
        return false
    }

    companion object {
        const val BEAN = "bean"
    }
}