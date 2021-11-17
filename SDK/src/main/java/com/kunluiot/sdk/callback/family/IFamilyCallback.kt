package com.kunluiot.sdk.callback.family

import com.kunluiot.sdk.bean.family.FamilyBean

/**
 * 创建家庭
 * */
interface ICreateFamilyCallback {
    fun onSuccess(bean: FamilyBean)
    fun onError(code: String, error: String)
}

/**
 * 家庭列表
 * */
interface IFamilyListCallback {
    fun onSuccess(bean: List<FamilyBean>)
    fun onError(code: String, error: String)
}

/**
 * 家庭详情
 * */
interface IFamilyDetailsCallback {
    fun onSuccess(bean: FamilyBean)
    fun onError(code: String, error: String)
}