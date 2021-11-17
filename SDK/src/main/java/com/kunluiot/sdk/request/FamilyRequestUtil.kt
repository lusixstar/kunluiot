package com.kunluiot.sdk.request

import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.bean.common.BaseRespBean
import com.kunluiot.sdk.bean.family.FamilyBean
import com.kunluiot.sdk.callback.IResultCallback
import com.kunluiot.sdk.callback.family.ICreateFamilyCallback
import com.kunluiot.sdk.callback.family.IFamilyDetailsCallback
import com.kunluiot.sdk.callback.family.IFamilyListCallback
import com.kunluiot.sdk.thirdlib.kalle.JsonBody
import com.kunluiot.sdk.thirdlib.kalle.Kalle
import com.kunluiot.sdk.thirdlib.kalle.simple.SimpleResponse
import com.kunluiot.sdk.util.JsonUtils

object FamilyRequestUtil {

    /**
     * 家庭列表
     */
    fun getFamilyList(callback: IFamilyListCallback) {
        Kalle.get(ReqApi.KHA_WEB_BASE_URL + FamilyApi.KHA_API_FAMILY)
            .setHeaders(KunLuHelper.getSign())
            .addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
            .perform(object : KunLuNetCallback<BaseRespBean<List<FamilyBean>>>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<BaseRespBean<List<FamilyBean>>, String>) {
                    val failed = response.failed()
                    if (!failed.isNullOrEmpty()) {
                        callback.onError(response.code().toString(), failed)
                    } else {
                        val data = response.succeed()
                        if (data.status != 200) {
                            callback.onError(data.status.toString(), data.message)
                        } else {
                            callback.onSuccess(data.data)
                        }
                    }
                }
            })
    }

    /**
     * 家庭详情
     */
    fun getFamilyDetails(familyId: String, callback: IFamilyDetailsCallback) {
        Kalle.get(ReqApi.KHA_WEB_BASE_URL + FamilyApi.KHA_API_FAMILY + "/$familyId")
            .setHeaders(KunLuHelper.getSign())
            .addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
            .perform(object : KunLuNetCallback<BaseRespBean<FamilyBean>>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<BaseRespBean<FamilyBean>, String>) {
                    val failed = response.failed()
                    if (!failed.isNullOrEmpty()) {
                        callback.onError(response.code().toString(), failed)
                    } else {
                        val data = response.succeed()
                        if (data.status != 200) {
                            callback.onError(data.status.toString(), data.message)
                        } else {
                            callback.onSuccess(data.data)
                        }
                    }
                }
            })
    }

    /**
     * 删除家庭
     */
    fun delete(familyId: String, callback: IResultCallback) {
        Kalle.delete(ReqApi.KHA_WEB_BASE_URL + FamilyApi.KHA_API_FAMILY + "/$familyId")
            .setHeaders(KunLuHelper.getSign())
            .addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
            .perform(object : KunLuNetCallback<BaseRespBean<Any>>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<BaseRespBean<Any>, String>) {
                    val failed = response.failed()
                    if (!failed.isNullOrEmpty()) {
                        callback.onError(response.code().toString(), failed)
                    } else {
                        val data = response.succeed()
                        if (data.status != 200) {
                            callback.onError(data.status.toString(), data.message)
                        } else {
                            callback.onSuccess()
                        }
                    }
                }
            })
    }

    /**
     * 更新家庭信息
     */
    fun update(familyId: String, name: String, detailAddress: String, callback: IResultCallback) {
        val map = mutableMapOf<String, String>()
        if(name.isNotEmpty()) map["familyName"] = name
        if(detailAddress.isNotEmpty()) map["detailAddress"] = detailAddress
        val param = JsonUtils.toJson(map)
        Kalle.put(ReqApi.KHA_WEB_BASE_URL + FamilyApi.KHA_API_FAMILY + "/$familyId")
            .setHeaders(KunLuHelper.getSign())
            .addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
            .body(JsonBody(param))
            .perform(object : KunLuNetCallback<BaseRespBean<Any>>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<BaseRespBean<Any>, String>) {
                    val failed = response.failed()
                    if (!failed.isNullOrEmpty()) {
                        callback.onError(response.code().toString(), failed)
                    } else {
                        callback.onSuccess()
                    }
                }
            })
    }

    /**
     * 创建家庭
     */
    fun addFamily(name: String, area: String, callback: ICreateFamilyCallback) {
        val map = mutableMapOf<String, String>()
        map["familyName"] = name
        map["detailAddress"] = area
        val param = JsonUtils.toJson(map)
        Kalle.post(ReqApi.KHA_WEB_BASE_URL + FamilyApi.KHA_API_FAMILY)
            .setHeaders(KunLuHelper.getSign())
            .addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
            .body(JsonBody(param))
            .perform(object : KunLuNetCallback<BaseRespBean<FamilyBean>>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<BaseRespBean<FamilyBean>, String>) {
                    val failed = response.failed()
                    if (!failed.isNullOrEmpty()) {
                        callback.onError(response.code().toString(), failed)
                    } else {
                        val data = response.succeed()
                        if (data.status != 200) {
                            callback.onError(data.status.toString(), data.message)
                        } else {
                            callback.onSuccess(data.data)
                        }
                    }
                }
            })
    }

    /**
     * 添加家庭成员
     */
    fun addFamilyMember(familyId: String, phoneNumber: String, name: String, gender: String, type: String, callback: IResultCallback) {
        val map = mutableMapOf<String, String>()
        map["phoneNumber"] = phoneNumber
        map["name"] = name
        map["gender"] = gender
        map["type"] = type
        val param = JsonUtils.toJson(map)
        Kalle.post(ReqApi.KHA_WEB_BASE_URL + FamilyApi.KHA_API_ADD_FAMILY_MEMBER + "/$familyId/member")
            .setHeaders(KunLuHelper.getSign())
            .addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
            .body(JsonBody(param))
            .perform(object : KunLuNetCallback<BaseRespBean<Any>>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<BaseRespBean<Any>, String>) {
                    val failed = response.failed()
                    if (!failed.isNullOrEmpty()) {
                        callback.onError(response.code().toString(), failed)
                    } else {
                        val data = response.succeed()
                        if (data.status != 200) {
                            callback.onError(data.status.toString(), data.message)
                        } else {
                            callback.onSuccess()
                        }
                    }
                }
            })
    }
}