package com.kunluiot.sdk.request

object ReqApi {

    const val KHA_DOMAIN = "creoiot.com" //dd // 不是正式地址

    private const val KHA_TEST_URL = "https://api.kunluiot.com"

    private const val KHA_BASE_URL = KHA_TEST_URL

    //uaa-url地址
    const val KHA_UAA_BASE_URL = "$KHA_BASE_URL/uaa"

    //webApi-url地址
    const val KHA_WEB_BASE_URL = "$KHA_BASE_URL/webapi"

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

object FamilyApi {
    // 创建家庭
    const val KHA_API_FAMILY = "/family"

}

object UserApi {
    //使用refresh_token刷新access_token
    const val KHA_API_REFRESH_TOKEN = "/token/refresh"

    //登录接口
    const val KHA_API_LOGIN = "/login"

    //获取图形验证码
    const val KHA_API_GET_VERIFY_IMAGE_CODE = "/api/v1/captcha"

    //检测图形验证码有效性
    const val KHA_API_CHECK_VERIFY_IMAGE_CODE = "/images/checkCaptcha"

    //获取验证码
    const val KHA_API_GET_VERIFY_CODE = "/sms/getVerifyCode"

    //检测验证码有效性
    const val KHA_API_CHECK_VERIFY_CODE = "/sms/checkVerifyCode"

    //注册
    const val KHA_API_REGISTER = "/register?type=phone"

    //忘记密码接口
    const val KHA_API_RESET_PASSWORD = "/resetPassword?type=phone"

    //获取用户信息
    const val KHA_API_GETUSERINFO = "/user/profile"

    //修改账户头像
    const val KHA_API_UPDATE_PHOTO = "/user/file"


}