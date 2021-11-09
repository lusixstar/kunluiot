package com.kunluiot.sdk.api.user

import com.kunluiot.sdk.callback.IResultCallback
import com.kunluiot.sdk.callback.user.IAvatarCallback
import com.kunluiot.sdk.callback.user.ILoginCallback
import com.kunluiot.sdk.callback.user.IUserCallback
import java.io.File

interface IKunLuUser {

    /**
     * 手机号和密码登录
     */
    fun loginWithPhonePassword(countryCode: String, phone: String, passwd: String, callback: ILoginCallback)

    /**
     * 获取验证码
     * phoneNumber 手机号码
     * type 注册：register 忘记密码：resetPassword 更换手机号码：changePhone
     */
    fun getVerifyCode(phoneNumber: String, type: String, areaCode: String, callback: IResultCallback)

    /**
     * 获取用户详情
     */
    fun getUserInfo(callback: IUserCallback)

    /**
     * 更新用户昵称
     */
    fun updateUserNick(nick: String, callback: IResultCallback)

    /**
     * 上传用户头像
     * */
    fun uploadHeader(file: File, callback: IAvatarCallback)

}