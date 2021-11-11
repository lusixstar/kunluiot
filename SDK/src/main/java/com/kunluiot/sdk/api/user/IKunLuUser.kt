package com.kunluiot.sdk.api.user

import com.kunluiot.sdk.callback.IResultCallback
import com.kunluiot.sdk.callback.user.*
import java.io.File

interface IKunLuUser {

    /**
     * 使用refresh_token刷新access_token
     */
    fun refreshToken(refreshToken: String, callback: ILoginCallback)

    /**
     * 手机号和密码登录
     */
    fun loginWithPhonePassword(countryCode: String, phone: String, passwd: String, callback: ILoginCallback)

    /**
     * 获取图像验证
     */
    fun getVerifyImageCode(callback: IVerifyImageCallback)

    /**
     * 检测图像验证有效性
     */
    fun checkVerifyImageCode(rid: String, code: String, callback: ICheckVerifyImageCallback)

    /**
     * 获取验证码
     * phoneNumber 手机号码
     * type 注册：register 忘记密码：resetPassword 更换手机号码：changePhone
     */
    fun getVerifyCode(phoneNumber: String, type: String, token: String, callback: IResultCallback)

    /**
     * 检测验证码有效性
     */
    fun checkVerifyCode(phoneNumber: String, type: String, areaCode: String, code :String, callback: ICodeCallback)

    /**
     * 注册
     */
    fun register(account: String, password: String, token: String, callback: IRegisterCallback)

    /**
     * 重置密码
     */
    fun resetPassword(account: String, password: String, token: String, callback: IResetPasswordCallback)

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
    fun uploadHeader(filePath: String, callback: IAvatarCallback)

}