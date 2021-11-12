package com.kunluiot.sdk.bean.device

/**
 * 设备列表
 * */
data class DeviceListBean(
    val category: DeviceCategoryBean = DeviceCategoryBean(),
    val categoryName: String = "",
    val productName: String = "",
    val categoryId: String = "",
    val products: List<DeviceProductsBean> = listOf(),
    val categoryParentName: String = "",
    val categorySelfName: String = "",
)

data class DeviceCategoryBean(
    val id: String = "",
    val name: DeviceNameBean = DeviceNameBean(),
    val logo: String = "",
    val parent: DeviceProductTabBean = DeviceProductTabBean(),
)

data class DeviceNameBean(
    val zh_CN: String = "",
    val en_US: String = "",
)

data class DeviceProductTabBean(
    var select: Boolean = false,
    val id: String = "",
    val name: DeviceNameBean = DeviceNameBean(),
    var categoryName: String = "",
)

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
)
