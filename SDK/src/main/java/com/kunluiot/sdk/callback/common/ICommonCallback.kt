package com.kunluiot.sdk.callback.common

import com.kunluiot.sdk.bean.common.CommonProblemBean

/**
 * 常见问题列表
 * */
interface ICommonProblemCallback {
    fun onSuccess(bean: CommonProblemBean)
    fun onError(code: String, error: String)
}
