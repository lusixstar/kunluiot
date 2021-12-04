package com.kunluiot.sdk.request


import android.content.Context
import com.kunluiot.sdk.R
import com.kunluiot.sdk.thirdlib.kalle.download.SimpleCallback
import com.kunluiot.sdk.thirdlib.kalle.exception.*


abstract class DownloadCallback(context: Context) : SimpleCallback() {

    private val mContext: Context = context

    override fun onException(e: Exception) {
        val message: String = when (e) {
            is NetworkError -> {
                mContext.getString(R.string.http_exception_network)
            }
            is URLError -> {
                mContext.getString(R.string.http_exception_url)
            }
            is HostError -> {
                mContext.getString(R.string.http_exception_host)
            }
            is ConnectTimeoutError -> {
                mContext.getString(R.string.http_exception_connect_timeout)
            }
            is WriteException -> {
                mContext.getString(R.string.http_exception_write)
            }
            is ReadTimeoutError -> {
                mContext.getString(R.string.http_exception_read_timeout)
            }
            else -> {
                mContext.getString(R.string.http_exception_unknow_error)
            }
        }
        onException(message)
    }

    /**
     * Error message.
     */
    abstract fun onException(message: String)
}