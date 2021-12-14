package com.kunluiot.sdk.bean.device

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable


/**
 * 删除设备
 * */
@Serializable
data class DeviceDeleteBean(
    val randomKey: String = "",
    val randomToken: String = "",
)

/**
 * 设备网关配网
 * */
@Serializable
data class DeviceConfigGateWayBean(
    val results: List<DeviceConfigGateWayItemBean> = listOf(),
    val success: Int = 0,
    val failure: Int = 0,
)

/**
 * 设备网关配网子项
 * */
@Serializable
data class DeviceConfigGateWayItemBean(
    val ctrlKey: String = "",
    val subDevTid: String = "",
    val result: String = "",
)

@Parcelize
@Serializable
data class DeviceOperationBean(
    val mid: String = "",
    val pid: String = "",
    val createTime: Long = 0,
    val workModeType: String = "",
    val fixedLength: Boolean = false,
    val statistics: Boolean = false,
    val accessProtocol: Int = 0,
    val protocol: Map<String, DeviceOperationProtocolBean> = mutableMapOf(),
    val fieldMap: Map<String, DeviceOperationFieldsBean> = mutableMapOf(),
) : Parcelable

@Parcelize
@Serializable
data class DeviceOperationProtocolBean(
    val cmdId: Int = 0,
    val cmdTag: String = "",
    val desc: String = "",
    val frameType: Int = 0,
    val usedForIFTTT: Boolean = false,
    val tags: List<String> = mutableListOf(),
    val fields: List<DeviceOperationFieldsBean> = mutableListOf(),
    val associateProtocol: List<Int> = mutableListOf(),
) : Parcelable

@Parcelize
@Serializable
data class DeviceOperationFieldsBean(
    var selectedDesc: String = "",
    var selectValue: String = "",
    var operator: String = "",
    val selected: Boolean = false,
    val dataType: String = "",
    val name: String = "",
    val desc: String = "",
    val dataLength: Int = 0,
    val available: Boolean = false,
    val frameType: String = "",
    val baseField: Boolean = false,
    val usedForIFTTT: Boolean = false,
    val aggregation: Boolean = false,
    var select: Boolean = false,
    val order: Int = 0,
    val dp: Int = 0,
    var maxValue: Long = 0,
    var minValue: Int = 0,
//    val maxValueMean: String = "",
//    val minValueMean: String = "",
    val enumeration: List<DeviceOperationEnumerationBean> = listOf(),
) : Parcelable

@Parcelize
@Serializable
data class DeviceOperationEnumerationBean(
    var value: Int = 0,
    var desc: String = "",
) : Parcelable

// --------------------------------------------

@Serializable
data class DeviceWifiBean(
    var name: String = "",
    var password: String = "",
)

@Serializable
data class ConfigWifiBean(
    val action: String = "",
    val params: ConfigWifiParamsBean = ConfigWifiParamsBean(),
    val ip: String = "",
)

@Serializable
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

@Serializable
data class ConfigZigBeeBean(
    val params: ConfigZigBeeParamsBean = ConfigZigBeeParamsBean(),
)

@Serializable
data class ConfigZigBeeParamsBean(
    val devTid: String = "",
    val subDevTid: String = "",
    val ctrlKey: String = "",
    val data: ConfigZigBeeDataBean = ConfigZigBeeDataBean(),
)

@Serializable
data class ConfigZigBeeDataBean(
    val cmdId: Int = 0,
    val devStatus: Int = 0,
    val battPercent: Int = 0,
)


//-------------------
@Serializable
data class DeviceFrameBean(
    val ctrlKey: String = "",
    val devTid: String = "",
    val subDevTid: String = "",
    val mid: String = "",
    val frameType: Int = 0,
)

@Serializable
data class ConfigNetworkBean(
    val ctrlKey: String = "",
    val result: String = "",
)

@Serializable
data class DevicePinCodeBean(
    var PINCode: String = "",
    var ssid: String = "",
)

/**
 * 设备产品列表
 * */
@Serializable
data class DeviceListProductBean(
    val category: DeviceCategoryBean = DeviceCategoryBean(),
    val categoryName: String = "",
    val productName: String = "",
    val categoryId: String = "",
    var products: List<DeviceProductsBean> = listOf(),
    val categoryParentName: String = "",
    val categorySelfName: String = "",
)

@Serializable
data class DeviceCategoryBean(
    val id: String = "",
    val name: DeviceNameBean = DeviceNameBean(),
    val logo: String = "",
    val parent: DeviceProductTabBean = DeviceProductTabBean(),
)

@Parcelize
@Serializable
data class DeviceNameBean(
    val zh_CN: String = "",
    val en_US: String = "",
) : Parcelable

@Serializable
data class DeviceProductTabBean(
    var select: Boolean = false,
    val id: String = "",
    val name: DeviceNameBean = DeviceNameBean(),
    var categoryName: String = "",
)

@Parcelize
@Serializable
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
@Serializable
data class DeviceNewBean(
    var devTid: String = "",
    val mid: String = "",
    val devType: String = "",
    val pid: String = "",
    var registerId: String = "",
    var subDevTid: String = "",
    var parentDevTid: String = "",
    var parentCtrlKey: String = "",
    var androidPageZipURL: String = "",
    var androidH5Page: String = "",
    var productPublicKey: String = "",
    var virtual: Boolean = false,
    val deviceName: String = "",
    val logo: String = "",
    val workModeType: String = "",
    var ctrlKey: String = "",
    val bindKey: String = "",
    val bindResultCode: Int = 0,
    var bindResultMsg: String = "",
    var binVer: String = "",
    var zigOtaBinVer : String= "", //zigbee协调器版本
    val name: String = "",
    val ownerUid: String = "",
    val familyName: String = "",
    val folderName: String = "",
    val binVersion: String = "",
    val binType: String = "",
    val associateGatewayCtrlKey: String = "",
    var online: Boolean = false,
    var select: Boolean = false,
    val productName: DeviceNameBean = DeviceNameBean(),
//    val subDevices: List<DeviceNameBean> = listOf(),
    val branchNames: List<String> = listOf(),
) : Parcelable

@Serializable
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

@Serializable
data class DeviceUpdateBean(
    val devTid: String = "",
    val update: Boolean = false,
    val devFirmwareOTARawRuleVO: DeviceDevFirmwareOTARawRuleVO = DeviceDevFirmwareOTARawRuleVO(),
)

@Serializable
data class DeviceDevFirmwareOTARawRuleVO(
    val binUrl: String = "",
    val md5: String = "",
    val latestBinType: String = "",
    val latestBinVer: String = "",
    val size: Int = 0,
)


