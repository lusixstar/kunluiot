package com.kunluiot.sdk.api.user

import com.kunluiot.sdk.callback.IResultCallback
import com.kunluiot.sdk.callback.common.OnFailResult
import com.kunluiot.sdk.callback.common.OnSuccessResult
import com.kunluiot.sdk.callback.common.OnSuccessStrResult
import com.kunluiot.sdk.callback.user.*
import com.kunluiot.sdk.request.UserRequestUtil

internal class KunLuUserImpl : IKunLuUser {

    /**
     * 刷新登录token
     * */
    override fun refreshToken(refreshToken: String, fail: OnFailResult, success: LoginSuccessResult) {
        UserRequestUtil.refreshToken(refreshToken, fail, success)
    }

    /**
     * 登录
     */
    override fun login(countryCode: String, phone: String, passwd: String, fail: OnFailResult, success: LoginSuccessResult) {
        UserRequestUtil.login(countryCode, phone, passwd, fail, success)
    }

    /**
     * 获取图像验证
     */
    override fun getVerifyImageCode(fail: OnFailResult, success: VerifyImageSuccessResult) {
        UserRequestUtil.getVerifyImageCode(fail, success)
    }

    /**
     * 检测图像验证有效性
     * */
    override fun checkVerifyImageCode(rid: String, code: String, fail: OnFailResult, success: CheckVerifyImageSuccessResult) {
        UserRequestUtil.checkVerifyImageCode(rid, code, fail, success)
    }

    /**
     * 获取验证码
     * phoneNumber 手机号码
     * type 注册：register 忘记密码：resetPassword 更换手机号码：changePhone
     */
    override fun getVerifyCode(phoneNumber: String, type: String, token: String, fail: OnFailResult, success: OnSuccessResult) {
        UserRequestUtil.getVerifyCode(phoneNumber, type, token, fail, success)
    }

    /**
     * 检测验证码有效性
     */
    override fun checkVerifyCode(phoneNumber: String, type: String, areaCode: String, code: String, fail: OnFailResult, success: VerifyCodeSuccessResult) {
        UserRequestUtil.checkVerifyCode(phoneNumber, type, areaCode, code, fail, success)
    }

    /**
     * 注册
     */
    override fun register(account: String, password: String, token: String, fail: OnFailResult, success: UserSuccessResult) {
        UserRequestUtil.register(account, password, token, fail, success)
    }

    /**
     * 获取用户详情
     */
    override fun getUserInfo(fail: OnFailResult, success: UserSuccessResult) {
        UserRequestUtil.getUserInfo(fail, success)
    }

    /**
     * 重置密码
     */
    override fun resetPassword(account: String, password: String, token: String, verifyCode: String, fail: OnFailResult, success: OnSuccessResult) {
        UserRequestUtil.resetPassword(account, password, token, verifyCode, fail, success)
    }

    /**
     * 更新用户昵称
     */
    override fun updateUserNick(nick: String, fail: OnFailResult, success: OnSuccessResult) {
        UserRequestUtil.updateUserNick(nick, fail, success)
    }

    /**
     * 上传用户头像
     * */
    override fun uploadHeader(filePath: String, fail: OnFailResult, success: AvatarSuccessResult) {
        UserRequestUtil.uploadHeader(filePath, fail, success)
    }

    /**
     * 更新用户头像
     * */
    override fun updateHeader(url: String, fail: OnFailResult, success: OnSuccessResult) {
        UserRequestUtil.updateHeader(url, fail, success)
    }

    /**
     * 修改密码
     */
    override fun changePassword(oldPassword: String, newPassword: String, fail: OnFailResult, success: OnSuccessResult) {
        UserRequestUtil.changePassword(oldPassword, newPassword, fail, success)
    }

    /**
     * 修改手机号码
     * */
    override fun changePhoneNum(verifyCode: String, token: String, phoneNumber: String, fail: OnFailResult, success: OnSuccessResult) {
        UserRequestUtil.changePhoneNum(verifyCode, token, phoneNumber, fail, success)
    }

    /**
     * 获取设备数量
     */
    override fun getDeviceCount(fail: OnFailResult, success: OnSuccessStrResult) {
        UserRequestUtil.getDeviceCount(fail, success)
    }

    /**
     * 绑定第三方账号
     */
    override fun bindOtherAccount(bindToken: String, fail: OnFailResult, success: OnSuccessResult) {
        UserRequestUtil.bindOtherAccount(bindToken, fail, success)
    }

    /**
     * 解绑第三方账号
     */
    override fun unBindOtherAccount(type: String, fail: OnFailResult, success: OnSuccessResult) {
        UserRequestUtil.unBindOtherAccount(type, fail, success)
    }
}