package com.kunluiot.sdk.api.family

import com.kunluiot.sdk.callback.family.ICreateFamilyCallback
import com.kunluiot.sdk.callback.family.IFamilyDetailsCallback
import com.kunluiot.sdk.callback.family.IFamilyListCallback

interface IKunLuFamily {

    /**
     * 创建家庭
     */
    fun createHome(name: String, area: String, callback: ICreateFamilyCallback)

    /**
     * 获取家庭列表
     */
    fun getHomeList(callback: IFamilyListCallback)

    /**
     * 获取家庭详情
     */
    fun getHomeDetails(familyId: String, callback: IFamilyDetailsCallback)
}