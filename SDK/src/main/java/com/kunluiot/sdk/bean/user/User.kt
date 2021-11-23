package com.kunluiot.sdk.bean.user


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SessionBean(
    @SerialName("access_token") var accessToken: String = "",
    @SerialName("refresh_token") var refreshToken: String = "",
    @SerialName("token_type") var tokenType: String = "",
    @SerialName("expires_in") var expiresIn: Int = 0,
    var user: String = "",
)

/**
 * 图像验证
 * */
data class VerifyImageBean(
    val rid: String = "",
    val png: String = "",
)

/**
 * 检测图像验证
 * */
data class CheckVerifyImageBean(
    val captchaToken: String = "",
)

/**
 * 用户信息
 * */
data class User(
    var name: String = "",
    var phoneNumber: String = "",
    var areaCode: String = "",
    var uid: String = "",
    var email: String = "",
    var avatarUrl: UserAvatarInfo = UserAvatarInfo(),
)

/**
 * 检测验证码
 * */
data class VerifyCodeBean(
    var phoneNumber: String = "",
    var verifyCode: String = "",
    var token: String = "",
    val timestamp: Long = 0,
    var code: Int = 0,
    var message: String = "",
    var desc: String = "",
)

/**
 * 用户头像
 * */
data class UserAvatarInfo(
    var small: String = "",
    var middle: String = "",
    var big: String = "",
)

/**
 * 上传头像地址
 * */
data class AvatarBean(
    var fileSourceUrl: String = "",
    var fileCDNUrl: String = "",
    var md5: String = "",
    var fileOriginName: String = "",
    var fileName: String = "",
    var uploadTime: Long = 0,
)