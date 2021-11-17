package com.kunluiot.sdk.api.family

import com.kunluiot.sdk.callback.IResultCallback
import com.kunluiot.sdk.callback.family.ICreateFamilyCallback
import com.kunluiot.sdk.callback.family.IFamilyDetailsCallback
import com.kunluiot.sdk.callback.family.IFamilyListCallback

interface IKunLuFamily {

    /**
     * 获取家庭列表
     */
    fun getFamilyList(callback: IFamilyListCallback)

    /**
     * 获取家庭详情
     */
    fun getFamilyDetails(familyId: String, callback: IFamilyDetailsCallback)

    /**
     * 删除家庭
     */
    fun delete(familyId: String, callback: IResultCallback)

    /**
     * 更新家庭信息
     */
    fun update(familyId: String, name: String, city: String, callback: IResultCallback)

    /**
     * 添加家庭
     */
    fun addFamily(name: String, area: String, callback: ICreateFamilyCallback)

    /**
     * 添加家庭成员
     */
    fun addFamilyMember(familyId: String, phoneNumber: String, name: String, gender: String, type: String, callback: IResultCallback)
}