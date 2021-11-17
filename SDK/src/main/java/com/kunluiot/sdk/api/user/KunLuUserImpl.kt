package com.kunluiot.sdk.api.user

import com.kunluiot.sdk.callback.IResultCallback
import com.kunluiot.sdk.callback.user.*
import com.kunluiot.sdk.request.UserRequestUtil

internal class KunLuUserImpl : IKunLuUser {

    /**
     * 刷新登录token
     * */
    override fun refreshToken(refreshToken: String, callback: ILoginCallback) {
        UserRequestUtil.refreshToken(refreshToken, callback)
    }

    /**
     * 手机号登录
     */
    override fun login(countryCode: String, phone: String, passwd: String, callback: ILoginCallback) {
        UserRequestUtil.login(countryCode, phone, passwd, callback)
    }

    /**
     * 绑定第三方账号
     */
    override fun bindOtherAccount(bindToken: String, callback: IResultCallback) {
        UserRequestUtil.bindOtherAccount(bindToken, callback)
    }

    /**
     * 解绑第三方账号
     */
    override fun unBindOtherAccount(type: String, callback: IResultCallback) {
        UserRequestUtil.unBindOtherAccount(type, callback)
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
    override fun resetPassword(account: String, password: String, token: String, verifyCode: String, callback: IResultCallback) {
        UserRequestUtil.resetPassword(account, password, token, verifyCode, callback)
    }

    /**
     * 修改密码
     */
    override fun changePassword(oldPassword: String, newPassword: String, callback: IResultCallback) {
        UserRequestUtil.changePassword(oldPassword, newPassword, callback)
    }

    /**
     * 获取用户详情
     */
    override fun getUserInfo(callback: IUserCallback) {
        UserRequestUtil.getUserInfo(callback)
    }

    /**
     * 获取设备数量
     */
    override fun getDeviceCount(callback: IUserDevicesCallback) {
        UserRequestUtil.getDeviceCount(callback)
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
    override fun uploadHeader(filePath: String, callback: IAvatarCallback) {
        UserRequestUtil.uploadHeader(filePath, callback)
    }

    /**
     * 更新用户头像
     * */
    override fun updateHeader(url: String, callback: IResultCallback) {
        UserRequestUtil.updateHeader(url, callback)
    }

    /**
     * 修改手机号码
     * */
    override fun changePhoneNum(verifyCode: String, token: String, phoneNumber: String, callback: IResultCallback) {
        UserRequestUtil.changePhoneNum(verifyCode, token, phoneNumber, callback)
    }
}