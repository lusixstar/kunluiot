package com.kunluiot.sdk.callback.family

import com.kunluiot.sdk.bean.family.FamilyCreateBean

/**
 * 创建家庭
 * */
interface ICreateFamilyCallback {
    fun onSuccess(bean: FamilyCreateBean)
    fun onError(code: String, error: String)
}

/**
 * 家庭列表
 * */
interface IFamilyListCallback {
    fun onSuccess(bean: List<FamilyCreateBean>)
    fun onError(code: String, error: String)
}

/**
 * 家庭详情
 * */
interface IFamilyDetailsCallback {
    fun onSuccess(bean: FamilyCreateBean)
    fun onError(code: String, error: String)
}