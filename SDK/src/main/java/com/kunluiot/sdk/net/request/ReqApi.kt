package com.kunluiot.sdk.net.request

object ReqApi {

    const val KHA_DOMAIN = "creoiot.com" //dd // 不是正式地址

    const val KHA_WEB_SOCKET_URL = "wss://hub-kl.$KHA_DOMAIN:2186"
    //uaa-url地址
    const val KHA_UAA_BASE_URL = "https://uaa-openapi-kl.$KHA_DOMAIN"
    //webApi-url地址
    const val KHA_WEB_BASE_URL = "https://webapi-openapi-kl.$KHA_DOMAIN"
    //console-url地址
    const val KHA_CONSOLE_BASE_URL = "https://console-openapi-kl.$KHA_DOMAIN"
}

object UserApi {
    //登录接口
    const val KHA_API_LOGIN = "/login"

    //修改账户头像
    const val KHA_API_UPDATE_PHOTO = "/user/file"

    //获取验证码接口
    const val KHA_API_GET_VERIFY_CODE = "/sms/getVerifyCode"

    //获取用户信息
    const val KHA_API_GETUSERINFO = "/user/profile"
}