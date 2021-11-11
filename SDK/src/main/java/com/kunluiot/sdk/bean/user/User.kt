package com.kunluiot.sdk.bean.user

import com.google.gson.annotations.SerializedName


data class SessionBean(
    @SerializedName("access_token") var accessToken: String = "",
    @SerializedName("refresh_token") var refreshToken: String = "",
    @SerializedName("token_type") var tokenType: String = "",
    @SerializedName("expires_in") var expiresIn: Int = 0,
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

data class UserAvatarInfo(
    val small: String = "",
    val middle: String = "",
    val big: String = "",
)

data class AvatarBean(
    var fileSourceUrl: String = "",
    var fileCDNUrl: String = "",
    var md5: String = "",
)