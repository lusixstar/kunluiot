package com.kunluiot.sdk.api.user

import com.kunluiot.sdk.callback.IResultCallback
import com.kunluiot.sdk.callback.user.*
import com.kunluiot.sdk.helper.UserRequestUtil
import java.io.File

internal class KunLuUserImpl : IKunLuUser {

    /**
     * 手机号和密码登录
     */
    override fun loginWithPhonePassword(countryCode: String, phone: String, passwd: String, callback: ILoginCallback) {
        UserRequestUtil.loginWithPhonePassword(countryCode, phone, passwd, callback)
    }

    /**
     * 获取图像验证
     */
    override fun getVerifyImageCode(callback: IVerifyImageCallback) {
        UserRequestUtil.getVerifyImageCode(callback)
    }

    /**
     * 检测图像验证有效性
     * */
    override fun checkVerifyImageCode(rid: String, code: String, callback: ICheckVerifyImageCallback) {
        UserRequestUtil.checkVerifyImageCode(rid, code, callback)
    }

    /**
     * 获取验证码
     * phoneNumber 手机号码
     * type 注册：register 忘记密码：resetPassword 更换手机号码：changePhone
     */
    override fun getVerifyCode(phoneNumber: String, type: String, token: String, callback: IResultCallback) {
        UserRequestUtil.getVerifyCode(phoneNumber, type, token, callback)
    }

    /**
     * 检测验证码有效性
     */
    override fun checkVerifyCode(phoneNumber: String, type: String, areaCode: String, code: String, callback: ICodeCallback) {
        UserRequestUtil.checkVerifyCode(phoneNumber, type, areaCode, code, callback)
    }

    /**
     * 注册
     */
    override fun register(account: String, password: String, token: String, callback: IRegisterCallback) {
        UserRequestUtil.register(account, password, token, callback)
    }

    /**
     * 重置密码
     */
    override fun resetPassword(account: String, password: String, token: String, callback: IResetPasswordCallback) {
        UserRequestUtil.resetPassword(account, password, token, callback)
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