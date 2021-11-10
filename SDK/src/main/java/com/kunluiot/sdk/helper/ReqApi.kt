package com.kunluiot.sdk.helper

object ReqApi {

    const val KHA_DOMAIN = "creoiot.com" //dd // 不是正式地址

    private const val KHA_TEST_URL = "https://api.kunluiot.com"

    private const val KHA_BASE_URL = KHA_TEST_URL

    //uaa-url地址
    const val KHA_UAA_BASE_URL = "$KHA_BASE_URL/uaa"

    //webApi-url地址
    const val KHA_WEB_BASE_URL = "$KHA_BASE_URL/webApi"

    //console-url地址
    const val KHA_CONSOLE_BASE_URL = "$KHA_BASE_URL/console"


    const val KHA_WEB_SOCKET_URL = "wss://hub-kl.$KHA_DOMAIN:2186"


    const val KHA_WEB_SOCKET_URL1 = "wss://hub-kl.$KHA_DOMAIN:2186"
    //uaa-url地址
    const val KHA_UAA_BASE_URL1 = "https://uaa-openapi-kl.$KHA_DOMAIN"
    //webApi-url地址
    const val KHA_WEB_BASE_URL1 = "https://webapi-openapi-kl.$KHA_DOMAIN"
    //console-url地址
    const val KHA_CONSOLE_BASE_URL1 = "https://console-openapi-kl.$KHA_DOMAIN"
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