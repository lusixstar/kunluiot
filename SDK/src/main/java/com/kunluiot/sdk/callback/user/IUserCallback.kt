package com.kunluiot.sdk.callback.user

import com.kunluiot.sdk.bean.user.*

/**
 * 登录
 * */
interface ILoginCallback {
    fun onSuccess(user: User)
    fun onError(code: String, error: String)
}

/**
 * 图像验证
 * */
interface IVerifyImageCallback {
    fun onSuccess(bean: VerifyImageBean)
    fun onError(code: String, error: String)
}

/**
 * 检测图像验证
 * */
interface ICheckVerifyImageCallback {
    fun onSuccess(bean: CheckVerifyImageBean)
    fun onError(code: String, error: String)
}

/**
 * 检测验证码
 * */
interface ICodeCallback {
    fun onSuccess(bean: VerifyCodeBean)
    fun onError(code: String, error: String)
}

/**
 * 注册
 * */
interface IRegisterCallback {
    fun onSuccess(bean: User)
    fun onError(code: String, error: String)
}

/**
 * 重置密码
 * */
interface IResetPasswordCallback {
    fun onSuccess(bean: User)
    fun onError(code: String, error: String)
}

/**
 * 用户信息
 * */
interface IUserCallback {
    fun onSuccess(user: User)
    fun onError(code: String, error: String)
}

/**
 * 上传文件
 * */
interface IAvatarCallback {
    fun onSuccess(avatar: AvatarBean)
    fun onError(code: String, error: String)
}
