package com.kunluiot.sdk.api.family

import com.kunluiot.sdk.callback.IResultCallback
import com.kunluiot.sdk.callback.family.ICreateFamilyCallback
import com.kunluiot.sdk.callback.family.IFamilyDetailsCallback
import com.kunluiot.sdk.callback.family.IFamilyListCallback
import com.kunluiot.sdk.request.FamilyRequestUtil

internal class KunLuFamilyImpl : IKunLuFamily {

    /**
     * 家庭列表
     */
    override fun getFamilyList(callback: IFamilyListCallback) {
        FamilyRequestUtil.getFamilyList(callback)
    }

    /**
     * 家庭详情
     */
    override fun getFamilyDetails(familyId: String, callback: IFamilyDetailsCallback) {
        FamilyRequestUtil.getFamilyDetails(familyId, callback)
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

    /**
     * 添加家庭
     */
    override fun addFamily(name: String, area: String, callback: ICreateFamilyCallback) {
        FamilyRequestUtil.addFamily(name, area, callback)
    }

    /**
     * 添加家庭成员
     */
    override fun addFamilyMember(familyId: String, phoneNumber: String, name: String, gender: String, type: String, callback: IResultCallback) {
        FamilyRequestUtil.addFamilyMember(familyId, phoneNumber, name, gender, type, callback)
    }
}