package com.kunluiot.sdk.api.user

import com.kunluiot.sdk.callback.common.OnFailResult
import com.kunluiot.sdk.callback.common.OnSuccessResult
import com.kunluiot.sdk.callback.common.OnSuccessStrResult
import com.kunluiot.sdk.callback.user.*

interface IKunLuUser {

    /**
     * 刷新登录token
     */
    fun refreshToken(refreshToken: String, fail: OnFailResult, success: LoginSuccessResult)

    /**
     * 登录
     */
    fun login(phone: String, passwd: String, fail: OnFailResult, success: LoginSuccessResult)

    /**
     * 获取验证码
     * phoneNumber 手机号码
     * type 注册：register 忘记密码：resetPassword 更换手机号码：changePhone
     */
    fun getVerifyCode(phoneNumber: String, type: String, fail: OnFailResult, success: OnSuccessResult)

    /**
     * 检测验证码有效性
     */
    fun checkVerifyCode(phoneNumber: String, type: String, code: String, fail: OnFailResult, success: VerifyCodeSuccessResult)

    /**
     * 注册
     */
    fun register(account: String, password: String, token: String, code: String, fail: OnFailResult, success: UserSuccessResult)

    /**
     * 重置密码
     */
    fun resetPassword(account: String, password: String, token: String, verifyCode: String, fail: OnFailResult, success: OnSuccessResult)

    /**
     * 获取用户信息
     */
    fun getUserInfo(fail: OnFailResult, success: UserSuccessResult)

    /**
     * 更新用户昵称
     */
    fun updateUserNick(nick: String, fail: OnFailResult, success: OnSuccessResult)

    /**
     * 上传用户头像
     * */
    fun uploadHeader(filePath: String, fail: OnFailResult, success: AvatarSuccessResult)

    /**
     * 更新用户头像
     * */
    fun updateHeader(url: String, fail: OnFailResult, success: OnSuccessResult)

    /**
     * 修改密码
     */
    fun changePassword(oldPassword: String, newPassword: String, fail: OnFailResult, success: OnSuccessResult)

    /**
     * 修改手机号码
     * */
    fun changePhoneNum(verifyCode: String, token: String, phoneNumber: String, fail: OnFailResult, success: OnSuccessResult)

    /**
     * 获取设备数量
     */
    fun getDeviceCount(fail: OnFailResult, success: OnSuccessStrResult)

    /**
     * 绑定第三方账号
     */
    fun bindOtherAccount(bindToken: String, fail: OnFailResult, success: OnSuccessResult)

    /**
     * 解绑第三方账号
     */
    fun unBindOtherAccount(type: String, fail: OnFailResult, success: OnSuccessResult)
}