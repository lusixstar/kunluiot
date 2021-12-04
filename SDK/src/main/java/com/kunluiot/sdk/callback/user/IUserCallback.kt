package com.kunluiot.sdk.callback.user

import com.kunluiot.sdk.bean.user.*

/**
 * 登录
 * */
fun interface LoginSuccessResult {
    fun success(user: User)
}

/**
 * 图像验证
 * */
fun interface VerifyImageSuccessResult {
    fun success(user: VerifyImageBean)
}

/**
 * 检测图像验证
 * */
fun interface CheckVerifyImageSuccessResult {
    fun success(user: CheckVerifyImageBean)
}

/**
 * 检测验证码
 * */
fun interface VerifyCodeSuccessResult {
    fun success(user: VerifyCodeBean)
}

/**
 * 注册用户
 * */
fun interface UserSuccessResult {
    fun success(user: User)
}

/**
 * 上传头像
 * */
fun interface AvatarSuccessResult {
    fun success(user: AvatarBean)
}