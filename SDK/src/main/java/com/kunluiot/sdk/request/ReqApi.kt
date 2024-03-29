package com.kunluiot.sdk.request

object ReqApi {

//    public static final String KHA_DOMAIN = "kunluiot.com";
//    public static final String KHA_WEB_SOCKET_URL = "ws://gdi." + KHA_DOMAIN + ":86";
//
//    //uaa-url地址
//    public static final String KHA_UAA_BASE_URL = "http://uaa-openapi." + KHA_DOMAIN;
//    //webApi-url地址
//    public static final String KHA_WEB_BASE_URL = "http://webapi-openapi." + KHA_DOMAIN;
//    //console-url地址
//    public static final String KHA_CONSOLE_BASE_URL = "http://console-openapi." + KHA_DOMAIN;

    const val IS_NEW_TEST = true
    private const val KHA_DOMAIN = "kunluiot.com"
    const val KHA_UAA_BASE_URL = "http://uaa-openapi.$KHA_DOMAIN"
    const val KHA_WEB_BASE_URL = "http://webapi-openapi.$KHA_DOMAIN"
    const val KHA_CONSOLE_BASE_URL = "http://console-openapi.$KHA_DOMAIN"
    const val KHA_WEB_SOCKET_URL = "ws://gdi.$KHA_DOMAIN:86"
    const val H5_USER = "http://webapi-openapi.kunluiot.com"
    const val H5_UAA = "http://uaa-openapi.kunluiot.com"
    const val H5_CONSOLE = "http://console-openapi.kunluiot.com"
    const val H5_DOMAIN = "kunluiot.com"

//    const val IS_NEW_TEST = true
//    private const val KHA_TEST_URL = "https://api.creoiot.com"
//    private const val KHA_BASE_URL = KHA_TEST_URL
//    const val KHA_UAA_BASE_URL = "$KHA_BASE_URL/uaa"
//    const val KHA_WEB_BASE_URL = "$KHA_BASE_URL/webapi"
//    const val KHA_CONSOLE_BASE_URL = "$KHA_BASE_URL/console"
//    const val KHA_WEB_SOCKET_URL = "wss://hub.creoiot.com:186"
//    const val H5_USER = "http://webapi-openapi.creoiot.com"
//    const val H5_UAA = "http://uaa-openapi.creoiot.com"
//    const val H5_CONSOLE = "http://console-openapi.creoiot.com"
//    const val H5_DOMAIN = "creoiot.com"

    //new-test-url
//    const val IS_NEW_TEST = true
//    private const val KHA_TEST_URL = "http://api-kl.creoiot.com"
//    private const val KHA_BASE_URL = KHA_TEST_URL
//    const val KHA_UAA_BASE_URL = "$KHA_BASE_URL/uaa"
//    const val KHA_WEB_BASE_URL = "$KHA_BASE_URL/webapi"
//    const val KHA_CONSOLE_BASE_URL = "$KHA_BASE_URL/console"
//    const val KHA_WEB_SOCKET_URL = "wss://hub-kl.creoiot.com:2186"
//    const val H5_USER = "http://webapi-openapi.kunluiot.com"
//    const val H5_UAA = "http://uaa-openapi.kunluiot.com"
//    const val H5_CONSOLE = "http://console-openapi.kunluiot.com"
//    const val H5_DOMAIN = "kunluiot.com"
}

object DeviceApi {

    //设备列表
    const val KHA_API_DEVICES = "/devices"

    // 设备列表
    const val KHA_API_GET_PRODUCTLIST = "/api/v2/product"

    //设备操作列表
    const val KHA_API_DEVICE_OPERATION_LIST = "/api/v1/protocol"

    //设备操作模板
    const val KHA_API_DEVICE_PROTOCOL_LIST = "/external/device/protocolTemplate"

    //获取pinCode
    const val KHA_API_GET_PIN_CODE = "/getPINCode"

    //获取新配上的设备列表
    const val KHA_API_GET_NEW_DEVICE_LIST = "/getNewDeviceList"

    //设备列表
    const val KHA_API_DEVICE = "/device"

    //检查设备固件是否需要升级
    const val KHA_API_CHECK_DEVICES_UPDATE = "/external/device/fw/ota/check"

    //协调器升级
    const val KHA_API_CHECK_ZIG_VER = "/external/device/fw/zig/ota/check"

    //删除授权设备
    const val KHA_API_DELETE_AUTHORIZATION_DEVICE = "/authorization"

    //删除子设备接口
    const val KHA_API_DELETE_SUB_DEVICE = "/device/delSubDevice"

    //获取群控
    const val KHA_API_GROUP_ACT = "/group"

    //设备配网
    const val KHA_API_DEVICE_CONTROL = "/deviceControl"

    //产品说明子页面列表接口
    const val KHA_API_PRODUCT_DESCRIBE = "/api/v2/product/describe/category"
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
    const val KHA_API_REGISTER_NEW = "/v1.0/register?type=phone"

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

object SceneApi {
    //手动场景列表
    const val KHA_API_ONE_KEY_SCENE_LIST = "/scene"

    //获取预设情景面板
    const val KHA_API_GET_SCENE_TEMPLATE = "/sceneTemplate"

    //联动场景列表
    const val KHA_API_LINKAGE_SCENE_LIST = "/rule/iftttRule_v2"

    //删除联动场景
    const val KHA_API_DELETE_LINKAGE_SCENE = "/rule/iftttRule"

    //新玩法列表接口
    const val KHA_API_NEW_PLAY = "/api/v2/newPlay/all"

    //更新一键场景排序
    const val KHA_API_UPDATE_ONEKEY_SCENE_SORT = "/sceneSort"

    //更新联动场景排序
    const val KHA_API_UPDATE_LINK_SCENE_SORT = "/rule/iftttRuleSort"
}

object CommonApi {
    //常见问题列表
    const val KHA_API_COMMON_PROBLEM = "/api/v2/commonProblem/app"

    //意见反馈
    const val KHA_API_FEEDBACK = "/external/feedback"

    //平台消息列表
    const val KHA_API_MESSAGE_PLATFORM = "/api/v1/app/push/history"

    //设备消息列表
    const val KHA_API_MESSAGE_DEVICE = "/user/allDevicesInfo"

    //设备消息置为已读
    const val KHA_API_MESSAGE_DEVICE_READ = "/api/v1/notification"

    //平台消息已读
    const val KHA_API_READ_MSG_FOLDER = "/api/v1/app/push/history"

    //平台消息清空
    const val KHA_API_CLEAR_MSG = "/api/v1/app/push/history/clear"

    //设备消息全部置为已读
    const val KHA_MESSAGE_DEVICE_ALL_READ = "/api/v1/warning"

    //设备消息清空
    const val KHA_API_MESSAGE_DEVICE_EMPTY = "/user/warnings"

    //绑定的第三方平台列表
    const val KHA_API_BIND_THIRD_PLATFORM_LIST = "/oauth/client/bind/list"
}