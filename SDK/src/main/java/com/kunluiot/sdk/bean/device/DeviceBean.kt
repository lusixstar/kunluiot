package com.kunluiot.sdk.bean.device

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class DeviceWifiBean(
    var name: String = "",
    var password: String = "",
)

data class ConfigWifiBean(
    val action: String = "",
    val params: ConfigWifiParamsBean = ConfigWifiParamsBean(),
    val ip: String = "",
)

data class ConfigWifiParamsBean(
    val devTid: String = "",
    val STEP: Int = 0,
    val encodeConfigType: Int = 0,
    val getIp: String = "",
    val PIN: String = "",
    val bind: Int = 0,
    val code: Int = 0,
    val DNSTimer: Int = 0,
    val DNSType: Int = 0,
    val binVer: String = "",
)

data class ConfigZigBeeBean(
    val params: ConfigZigBeeParamsBean = ConfigZigBeeParamsBean(),
)

data class ConfigZigBeeParamsBean(
    val devTid: String = "",
    val subDevTid: String = "",
    val ctrlKey: String = "",
    val data: ConfigZigBeeDataBean = ConfigZigBeeDataBean(),
)

data class ConfigZigBeeDataBean(
    val cmdId: Int = 0,
    val devStatus: Int = 0,
    val battPercent: Int = 0,
)


//-------------------

data class DeviceFrameBean(
    val ctrlKey: String = "",
    val devTid: String = "",
    val subDevTid: String = "",
    val mid: String = "",
    val frameType: Int = 0,
)

data class  ConfigNetworkBean(
    val ctrlKey: String = "",
    val result: String = "",
)

data class DevicePinCodeBean(
    var PINCode: String = "",
    var ssid: String = "",
)

/**
 * 设备产品列表
 * */
data class DeviceListProductBean(
    val category: DeviceCategoryBean = DeviceCategoryBean(),
    val categoryName: String = "",
    val productName: String = "",
    val categoryId: String = "",
    var products: List<DeviceProductsBean> = listOf(),
    val categoryParentName: String = "",
    val categorySelfName: String = "",
)

data class DeviceCategoryBean(
    val id: String = "",
    val name: DeviceNameBean = DeviceNameBean(),
    val logo: String = "",
    val parent: DeviceProductTabBean = DeviceProductTabBean(),
)

@Parcelize
data class DeviceNameBean(
    val zh_CN: String = "",
    val en_US: String = "",
) : Parcelable

data class DeviceProductTabBean(
    var select: Boolean = false,
    val id: String = "",
    val name: DeviceNameBean = DeviceNameBean(),
    var categoryName: String = "",
)

@Parcelize
data class DeviceProductsBean(
    val mid: String = "",
    val name: String = "",
    val productType: String = "",
    val displayName: DeviceNameBean = DeviceNameBean(),
    val model: String = "",
    val logo: String = "",
    val status: String = "",
    val bindType: String = "",
    val configDesc: String = "",
    val ctrlKey: String = "",
    val deviceName: String = "",
    val appDisplayName: DeviceNameBean = DeviceNameBean(),
    val accessWay: List<String> = listOf(),
    val configDescImg: List<String> = listOf(),
) : Parcelable

@Parcelize
data class DeviceNewBean(
    val devTid: String = "",
    val mid: String = "",
    val devType: String = "",
    val pid: String = "",
    var registerId: String = "",
    val deviceName: String = "",
    val ctrlKey: String = "",
    val bindResultCode: Int = 0,
    var bindResultMsg: String = "",
    val name: String = "",
    val online: Boolean = false,
    val productName: DeviceNameBean = DeviceNameBean(),
) : Parcelable

data class DeviceProductDescribeBean(
    val id: String = "",
    val pid: String = "",
    val mid: String = "",
    val title: String = "",
    val createTime: String = "",
    val updateTime: String = "",
    val content: String = "",
    val logo: String = "",
    val category: String = "",
    val enable: Boolean = false,
    val openContent: String = "",
)