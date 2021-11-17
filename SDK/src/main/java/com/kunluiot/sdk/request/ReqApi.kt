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

object DeviceApi {

    //获取设备上报下发帧
    const val KHA_API_DEVICE_PROTOCOL = "/family/device/protocol"

    // 设备列表
    const val KHA_API_GET_PRODUCTLIST = "/api/v2/product"

    //获取pinCode
    const val KHA_API_GET_PIN_CODE = "/getPINCode"

    //获取新配上的设备列表
    const val KHA_API_GET_NEW_DEVICE_LIST = "/getNewDeviceList"

    //设备获取
    const val KHA_API_DEVICES = "/devices"

    //设备列表
    const val KHA_API_DEVICE = "/device"

    //设备配网
    const val KHA_API_DEVICE_CONTROL = "/deviceControl"
}

object FamilyApi {
    // 家庭
    const val KHA_API_FAMILY = "/family"

    //房间列表
    const val KHA_API_FOLDER = "/folder"

    //房间列表并且返回房间下的所有设备
    const val KHA_API_FOLDER_DEVICE = "/newFolder"

    //房间排序
    const val KHA_API_FOLDER_SORT = "/folder/sort"

    //获取设备上报下发帧
    const val KHA_API_DEVICE_PROTOCOL = "/family/device/protocol"
}

object UserApi {
    //使用refresh_token刷新access_token
    const val KHA_API_REFRESH_TOKEN = "/token/refresh"

    //登录接口
    const val KHA_API_LOGIN = "/login"

    //绑定第三方账号
    const val KHA_API_BIND_OTHER_ACCOUNT = "/account/bind"

    //解绑第三方账号
    const val KHA_API_UNBIND_OTHER_ACCOUNT = "/account/unbind"

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

    //重置密码
    const val KHA_API_RESET_PASSWORD = "/resetPassword?type=phone"

    //修改密码
    const val KHA_API_CHANGE_PASSWORD = "/changePassword"

    //获取用户信息
    const val KHA_API_GETUSERINFO = "/user/profile"

    //获取设备数量
    const val KHA_API_GET_DEVICE_COUNT = "/device/all/count"

    //修改账户头像
    const val KHA_API_UPDATE_PHOTO = "/user/file"

    //修改手机号码
    const val KHA_API_CHANGE_PHONE_NUM = "/changePhoneNumber"

}

object CommonApi {
    //常见问题列表
    const val KHA_API_COMMON_PROBLEM = "/api/v2/commonProblem/app"

    //意见反馈
    const val KHA_API_FEEDBACK = "/external/feedback"
}