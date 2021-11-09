package com.kunluiot.sdk.api.user

import com.kunluiot.sdk.callback.IResultCallback
import com.kunluiot.sdk.callback.user.IAvatarCallback
import com.kunluiot.sdk.callback.user.ILoginCallback
import com.kunluiot.sdk.callback.user.IUserCallback
import com.kunluiot.sdk.net.request.UserRequestUtil
import java.io.File

class KunLuUserImpl : IKunLuUser {

    /**
     * 手机号和密码登录
     */
    override fun loginWithPhonePassword(countryCode: String, phone: String, passwd: String, callback: ILoginCallback) {
        UserRequestUtil.loginWithPhonePassword(countryCode, phone, passwd, callback)
    }

    /**
     * 获取验证码
     * phoneNumber 手机号码
     * type 注册：register 忘记密码：resetPassword 更换手机号码：changePhone
     */
    override fun getVerifyCode(phoneNumber: String, type: String, areaCode: String, callback: IResultCallback) {
        UserRequestUtil.getVerifyCode(phoneNumber, type, areaCode, callback)
    }

    /**
     * 获取用户详情
     */
    override fun getUserInfo(callback: IUserCallback) {
        UserRequestUtil.getUserInfo(callback)
    }

    /**
     * 更新用户昵称
     */
    override fun updateUserNick(nick: String, callback: IResultCallback) {
        UserRequestUtil.updateUserNick(nick, callback)
    }

    /**
     * 上传用户头像
     * */
    override fun uploadHeader(file: File, callback: IAvatarCallback) {
        UserRequestUtil.uploadHeader(file, callback)
    }
}