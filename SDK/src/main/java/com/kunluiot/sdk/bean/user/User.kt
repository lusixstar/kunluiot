package com.kunluiot.sdk.bean.user

import com.google.gson.annotations.SerializedName


data class SessionBean(
    @SerializedName("access_token") var accessToken: String = "",
    @SerializedName("refresh_token") var refreshToken: String = "",
    @SerializedName("token_type") var tokenType: String = "",
    @SerializedName("expires_in") var expiresIn: Int = 0,
    var user: String = "",
)

data class User(
    var name: String = "",
    var phoneNumber: String = "",
    var areaCode: String = "",
    var uid: String = "",
    var email: String = "",
    var avatarUrl: UserAvatarInfo = UserAvatarInfo(),
)

data class UserAvatarInfo(
    val small: String = "",
    val middle: String = "",
    val big: String = "",
)

data class VerifyCodeBean(
    var code: Int = 0,
    var message: String = "",
    var desc: String = "",
    val timestamp: Long = 0,
)

data class AvatarBean(
    var fileSourceUrl: String = "",
    var fileCDNUrl: String = "",
    var md5: String = "",
)