package com.kunluiot.sdk.request

import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.bean.common.BaseRespBean
import com.kunluiot.sdk.bean.family.FamilyBean
import com.kunluiot.sdk.bean.family.FolderBean
import com.kunluiot.sdk.callback.IResultCallback
import com.kunluiot.sdk.callback.family.*
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
        Kalle.post(ReqApi.KHA_WEB_BASE_URL + FamilyApi.KHA_API_FAMILY + "/$familyId/member")
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

    /**
     * 删除家庭成员
     */
    fun deleteFamilyMember(familyId: String, uid: String, callback: IResultCallback) {
        Kalle.delete(ReqApi.KHA_WEB_BASE_URL + FamilyApi.KHA_API_FAMILY + "/$familyId/member")
            .setHeaders(KunLuHelper.getSign())
            .addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
            .param("uid", uid)
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
     *  修改家庭成员名称和头像
     */
    fun updateFamilyMemberInfo(familyId: String, name: String, gender: String, uid: String, callback: IResultCallback) {
        val map = mutableMapOf<String, String>()
        map["name"] = name
        map["gender"] = gender
        map["uid"] = uid
        val param = JsonUtils.toJson(map)
        Kalle.put(ReqApi.KHA_WEB_BASE_URL + FamilyApi.KHA_API_FAMILY + "/$familyId/member/info")
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
     *  房间列表
     */
    fun getRooms(familyId: String, page: Int, size: Int, callback: IFamilyRoomListCallback) {
        Kalle.get(ReqApi.KHA_WEB_BASE_URL + FamilyApi.KHA_API_FOLDER)
            .setHeaders(KunLuHelper.getSign())
            .addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
            .param("familyId", familyId)
            .param("page", page)
            .param("size", size)
            .perform(object : KunLuNetCallback<BaseRespBean<List<FolderBean>>>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<BaseRespBean<List<FolderBean>>, String>) {
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
     *  房间列表并且返回房间下的所有设备
     */
    fun getRoomsDevice(familyId: String, filterFlag: Boolean, page: Int, size: Int, callback: IFamilyRoomListCallback) {
        Kalle.get(ReqApi.KHA_WEB_BASE_URL + FamilyApi.KHA_API_FOLDER_DEVICE)
            .setHeaders(KunLuHelper.getSign())
            .addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
            .param("familyId", familyId)
            .param("filterFlag", filterFlag)
            .param("page", page)
            .param("size", size)
            .perform(object : KunLuNetCallback<BaseRespBean<List<FolderBean>>>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<BaseRespBean<List<FolderBean>>, String>) {
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
     *  添加房间
     */
    fun addRooms(familyId: String, folderName: String, callback: IResultCallback) {
        val map = mutableMapOf<String, Any>()
        map["familyId"] = familyId
        map["folderName"] = folderName
        val param = JsonUtils.toJson(map)
        Kalle.post(ReqApi.KHA_WEB_BASE_URL + FamilyApi.KHA_API_FOLDER)
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

    /**
     *  修改房间信息
     */
    fun updateRoomInfo(folderId: String, folderName: String, callback: IResultCallback) {
        val map = mutableMapOf<String, String>()
        map["folderName"] = folderName
        val param = JsonUtils.toJson(map)
        Kalle.put(ReqApi.KHA_WEB_BASE_URL + FamilyApi.KHA_API_FOLDER + "/$folderId")
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

    /**
     *  删除房间
     */
    fun deleteRoom(folderId: String,  callback: IResultCallback) {
        Kalle.delete(ReqApi.KHA_WEB_BASE_URL + FamilyApi.KHA_API_FOLDER + "/$folderId")
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
     * 移动房间设备
     */
    fun moveRoomDevice(folderId: String, devTid: String, ctrlKey: String, subDevTid: String, callback: IResultCallback) {
        val map = mutableMapOf<String, String>()
        map["devTid"] = devTid
        map["ctrlKey"] = ctrlKey
        if(subDevTid.isNotEmpty()) map["subDevTid"] = subDevTid
        val param = JsonUtils.toJson(map)
        Kalle.post(ReqApi.KHA_WEB_BASE_URL + FamilyApi.KHA_API_FOLDER + "/$folderId")
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

    /**
     * 房间排序
     */
    fun sortRoom(sortFolders: List<FolderBean>, callback: IFamilyRoomSortCallback) {
        val map = mutableMapOf<String, Any>()
        map["requestList"] = sortFolders
        val param = JsonUtils.toJson(map)
        Kalle.patch(ReqApi.KHA_WEB_BASE_URL + FamilyApi.KHA_API_FOLDER_SORT)
            .setHeaders(KunLuHelper.getSign())
            .addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
            .body(JsonBody(param))
            .perform(object : KunLuNetCallback<BaseRespBean<List<String>>>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<BaseRespBean<List<String>>, String>) {
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
}