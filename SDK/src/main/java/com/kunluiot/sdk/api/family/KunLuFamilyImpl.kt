package com.kunluiot.sdk.api.family

import com.kunluiot.sdk.callback.IResultCallback
import com.kunluiot.sdk.callback.family.ICreateFamilyCallback
import com.kunluiot.sdk.callback.family.IFamilyDetailsCallback
import com.kunluiot.sdk.callback.family.IFamilyListCallback
import com.kunluiot.sdk.helper.FamilyRequestUtil

internal class KunLuFamilyImpl : IKunLuFamily {

    /**
     * 创建家庭
     */
    override fun createHome(name: String, area: String, callback: ICreateFamilyCallback) {
        FamilyRequestUtil.createHome(name, area, callback)
    }

    /**
     * 获取家庭列表
     */
    override fun getHomeList(callback: IFamilyListCallback) {
        FamilyRequestUtil.getHomeList(callback)
    }

    /**
     * 获取家庭详情
     */
    override fun getHomeDetails(familyId: String, callback: IFamilyDetailsCallback) {
        FamilyRequestUtil.getHomeDetails(familyId, callback)
    }

    /**
     * 删除家庭
     */
    override fun delete(familyId: String, callback: IResultCallback) {
        FamilyRequestUtil.delete(familyId, callback)
    }

    /**
     * 更新家庭信息
     */
    override fun update(familyId: String, name: String, city: String, callback: IResultCallback) {
        FamilyRequestUtil.update(familyId, name, city, callback)
    }
}