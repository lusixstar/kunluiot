package com.kunluiot.sdk.request

import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.bean.common.BaseRespBean
import com.kunluiot.sdk.bean.device.DeviceFrameBean
import com.kunluiot.sdk.bean.family.FamilyBean
import com.kunluiot.sdk.bean.family.FolderBean
import com.kunluiot.sdk.callback.IResultCallback
import com.kunluiot.sdk.callback.common.OnFailResult
import com.kunluiot.sdk.callback.family.*
import com.kunluiot.sdk.thirdlib.kalle.JsonBody
import com.kunluiot.sdk.thirdlib.kalle.Kalle
import com.kunluiot.sdk.thirdlib.kalle.simple.SimpleResponse
import com.kunluiot.sdk.util.JsonUtils
import com.kunluiot.sdk.util.KotlinSerializationUtils

object FamilyRequestUtil {

    /**
     * 家庭列表
     */
    fun getFamilyList(fail: OnFailResult, success: FamilyListResult) {
        Kalle.get(ReqApi.KHA_WEB_BASE_URL + FamilyApi.KHA_API_FAMILY)
            .addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
            .perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<String, String>) {
                    val failed = response.failed()
                    if (!failed.isNullOrEmpty()) {
                        fail.fail(response.code().toString(), failed)
                    } else {
                        KotlinSerializationUtils.getJsonData<List<FamilyBean>>(response.succeed()).let { success.result(it) }
                    }
                }
            })
    }

    /**
     *  房间列表并且返回房间下的所有设备
     */
    fun getRoomsDevice(familyId: String, filterFlag: Boolean, page: Int, size: Int, fail: OnFailResult, success: RoomListResult) {
        Kalle.get(ReqApi.KHA_WEB_BASE_URL + FamilyApi.KHA_API_FOLDER_DEVICE)
            .addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
            .param("familyId", familyId)
            .param("filterFlag", filterFlag)
            .param("page", page)
            .param("size", size)
            .perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<String, String>) {
                    val failed = response.failed()
                    if (!failed.isNullOrEmpty()) {
                        fail.fail(response.code().toString(), failed)
                    } else {
                        KotlinSerializationUtils.getJsonData<List<FolderBean>>(response.succeed()).let { success.result(it) }
                    }
                }
            })
    }



    //--------------------------------------------------------------------

    /**
     * 家庭详情
     */
    fun getFamilyDetails(familyId: String, callback: IFamilyDetailsCallback) {
        Kalle.get(ReqApi.KHA_WEB_BASE_URL + FamilyApi.KHA_API_FAMILY + "/$familyId")
            .addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
            .perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<String, String>) {
                    val failed = response.failed()
                    if (!failed.isNullOrEmpty()) {
                        callback.onError(response.code().toString(), failed)
                    } else {
                        KotlinSerializationUtils.getJsonData<FamilyBean>(response.succeed()).let { callback.onSuccess(it) }
                    }
                }
            })
    }

    /**
     * 删除家庭
     */
    fun delete(familyId: String, callback: IResultCallback) {
        Kalle.delete(ReqApi.KHA_WEB_BASE_URL + FamilyApi.KHA_API_FAMILY + "/$familyId")
            .addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
            .perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<String, String>) {
                    val failed = response.failed()
                    if (!failed.isNullOrEmpty()) {
                        callback.onError(response.code().toString(), failed)
                    } else {
                        KotlinSerializationUtils.getJsonData<String>(response.succeed()).let { callback.onSuccess() }
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
            .addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
            .body(JsonBody(param))
            .perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<String, String>) {
                    val failed = response.failed()
                    if (!failed.isNullOrEmpty()) {
                        callback.onError(response.code().toString(), failed)
                    } else {
                        KotlinSerializationUtils.getJsonData<String>(response.succeed()).let { callback.onSuccess() }
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
            .addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
            .body(JsonBody(param))
            .perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<String, String>) {
                    val failed = response.failed()
                    if (!failed.isNullOrEmpty()) {
                        callback.onError(response.code().toString(), failed)
                    } else {
                        KotlinSerializationUtils.getJsonData<FamilyBean>(response.succeed()).let { callback.onSuccess(it) }
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
        Kalle.post(ReqApi.KHA_WEB_BASE_URL + FamilyApi.KHA_API_FAMILY + "/$familyId/member")
            .addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
            .body(JsonBody(param))
            .perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<String, String>) {
                    val failed = response.failed()
                    if (!failed.isNullOrEmpty()) {
                        callback.onError(response.code().toString(), failed)
                    } else {
                        KotlinSerializationUtils.getJsonData<String>(response.succeed()).let { callback.onSuccess() }
                    }
                }
            })
    }

    /**
     * 删除家庭成员
     */
    fun deleteFamilyMember(familyId: String, uid: String, callback: IResultCallback) {
        Kalle.delete(ReqApi.KHA_WEB_BASE_URL + FamilyApi.KHA_API_FAMILY + "/$familyId/member")
            .addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
            .param("uid", uid)
            .perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<String, String>) {
                    val failed = response.failed()
                    if (!failed.isNullOrEmpty()) {
                        callback.onError(response.code().toString(), failed)
                    } else {
                        KotlinSerializationUtils.getJsonData<String>(response.succeed()).let { callback.onSuccess() }
                    }
                }
            })
    }

    /**
     *  修改家庭成员名称和头像
     */
    fun updateFamilyMemberInfo(familyId: String, name: String, gender: String, uid: String, callback: IResultCallback) {
        val map = mutableMapOf<String, String>()
        map["name"] = name
        map["gender"] = gender
        map["uid"] = uid
        val param = JsonUtils.toJson(map)
        Kalle.put(ReqApi.KHA_WEB_BASE_URL + FamilyApi.KHA_API_FAMILY + "/$familyId/member/info")
            .addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
            .body(JsonBody(param))
            .perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<String, String>) {
                    val failed = response.failed()
                    if (!failed.isNullOrEmpty()) {
                        callback.onError(response.code().toString(), failed)
                    } else {
                        KotlinSerializationUtils.getJsonData<String>(response.succeed()).let { callback.onSuccess() }
                    }
                }
            })
    }

    /**
     *  家庭成员设备授权
     */
    fun updateMemberCtrlKeys(familyId: String, uid: String, ctrlKeys: List<String>, callback: IResultCallback) {
        val map = mutableMapOf<String, Any>()
        map["ctrlKeys"] = ctrlKeys
        map["uid"] = uid
        val param = JsonUtils.toJson(map)
        Kalle.put(ReqApi.KHA_WEB_BASE_URL + FamilyApi.KHA_API_FAMILY + "/$familyId/member")
            .setHeaders(KunLuHelper.getSign())
            .addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
            .body(JsonBody(param))
            .perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<String, String>) {
                    val failed = response.failed()
                    if (!failed.isNullOrEmpty()) {
                        callback.onError(response.code().toString(), failed)
                    } else {
                        KotlinSerializationUtils.getJsonData<String>(response.succeed()).let { callback.onSuccess() }

                    }
                }
            })
    }

    /**
     *  房间列表
     */
    fun getRooms(familyId: String, page: Int, size: Int, callback: IFamilyRoomListCallback) {
        Kalle.get(ReqApi.KHA_WEB_BASE_URL + FamilyApi.KHA_API_FOLDER)
            .addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
            .param("familyId", familyId)
            .param("page", page)
            .param("size", size)
            .perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<String, String>) {
                    val failed = response.failed()
                    if (!failed.isNullOrEmpty()) {
                        callback.onError(response.code().toString(), failed)
                    } else {
                        KotlinSerializationUtils.getJsonData<List<FolderBean>>(response.succeed()).let { callback.onSuccess(it) }
                    }
                }
            })
    }



    /**
     *  添加房间
     */
    fun addRooms(familyId: String, folderName: String, callback: IResultCallback) {
        val map = mutableMapOf<String, Any>()
        map["familyId"] = familyId
        map["folderName"] = folderName
        val param = JsonUtils.toJson(map)
        Kalle.post(ReqApi.KHA_WEB_BASE_URL + FamilyApi.KHA_API_FOLDER)
            .addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
            .body(JsonBody(param))
            .perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<String, String>) {
                    val failed = response.failed()
                    if (!failed.isNullOrEmpty()) {
                        callback.onError(response.code().toString(), failed)
                    } else {
                        KotlinSerializationUtils.getJsonData<String>(response.succeed()).let { callback.onSuccess() }
                    }
                }
            })
    }

    /**
     *  修改房间信息
     */
    fun updateRoomInfo(folderId: String, folderName: String, callback: IResultCallback) {
        val map = mutableMapOf<String, String>()
        map["folderName"] = folderName
        val param = JsonUtils.toJson(map)
        Kalle.put(ReqApi.KHA_WEB_BASE_URL + FamilyApi.KHA_API_FOLDER + "/$folderId")
            .addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
            .body(JsonBody(param))
            .perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<String, String>) {
                    val failed = response.failed()
                    if (!failed.isNullOrEmpty()) {
                        callback.onError(response.code().toString(), failed)
                    } else {
                        KotlinSerializationUtils.getJsonData<String>(response.succeed()).let { callback.onSuccess() }
                    }
                }
            })
    }

    /**
     *  删除房间
     */
    fun deleteRoom(folderId: String,  callback: IResultCallback) {
        Kalle.delete(ReqApi.KHA_WEB_BASE_URL + FamilyApi.KHA_API_FOLDER + "/$folderId")
            .addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
            .perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<String, String>) {
                    val failed = response.failed()
                    if (!failed.isNullOrEmpty()) {
                        callback.onError(response.code().toString(), failed)
                    } else {
                        KotlinSerializationUtils.getJsonData<String>(response.succeed()).let { callback.onSuccess() }
                    }
                }
            })
    }

    /**
     * 移动房间设备
     */
    fun moveRoomDevice(folderId: String, devTid: String, ctrlKey: String, subDevTid: String, callback: IResultCallback) {
        val map = mutableMapOf<String, String>()
        map["devTid"] = devTid
        map["ctrlKey"] = ctrlKey
        if(subDevTid.isNotEmpty()) map["subDevTid"] = subDevTid
        val param = JsonUtils.toJson(map)
        Kalle.post(ReqApi.KHA_WEB_BASE_URL + FamilyApi.KHA_API_FOLDER + "/$folderId")
            .addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
            .body(JsonBody(param))
            .perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<String, String>) {
                    val failed = response.failed()
                    if (!failed.isNullOrEmpty()) {
                        callback.onError(response.code().toString(), failed)
                    } else {
                        KotlinSerializationUtils.getJsonData<String>(response.succeed()).let { callback.onSuccess() }
                    }
                }
            })
    }

    /**
     * 房间排序
     */
    fun sortRoom(sortFolders: List<FolderBean>, callback: IFamilyRoomSortCallback) {
        val map = mutableMapOf<String, Any>()
        map["requestList"] = sortFolders
        val param = JsonUtils.toJson(map)
        Kalle.patch(ReqApi.KHA_WEB_BASE_URL + FamilyApi.KHA_API_FOLDER_SORT)
            .addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
            .body(JsonBody(param))
            .perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<String, String>) {
                    val failed = response.failed()
                    if (!failed.isNullOrEmpty()) {
                        callback.onError(response.code().toString(), failed)
                    } else {
                        KotlinSerializationUtils.getJsonData<List<String>>(response.succeed()).let { callback.onSuccess(it) }
                    }
                }
            })
    }

    /**
     * 获取设备上报下发帧
     */
    fun getDeviceFrame(familyId: String, callback: IFamilyRoomDeviceFrameCallback) {
        Kalle.get(ReqApi.KHA_WEB_BASE_URL + FamilyApi.KHA_API_DEVICE_PROTOCOL)
            .addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
            .param("familyId", familyId)
            .perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<String, String>) {
                    val failed = response.failed()
                    if (!failed.isNullOrEmpty()) {
                        callback.onError(response.code().toString(), failed)
                    } else {
                        KotlinSerializationUtils.getJsonData<List<DeviceFrameBean>>(response.succeed()).let { callback.onSuccess(it) }
                    }
                }
            })
    }
}