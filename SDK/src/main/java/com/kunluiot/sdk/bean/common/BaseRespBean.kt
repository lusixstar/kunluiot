package com.kunluiot.sdk.bean.common


data class BaseRespBean<T>(
    var status: Int = 0,
    var message: String = "",
    var data: T,
    var desc: String = "",
    var code: Int = 0,
    var timestamp: String = "",
)

data class BaseSocketBean(
    var msgId: Int = 0,
    var action: String = "",
    var code: Int = 0,
)

data class CommonProblemBean(
    val content: List<CommonProblemContent> = listOf(),
    val first: Boolean = false,
    val last: Boolean = false,
    val number: Int = 0,
    val numberOfElements: Int = 0,
    val page: Int = 0,
    val size: Int = 0,
    val totalElements: Int = 0,
    val totalPages: Int = 0,
)

data class CommonProblemContent(
    val content: String = "",
    val id: String = "",
    val pid: String = "",
    val show: Boolean = false,
    val tenantId: String = "",
    val title: String = "",
    val updateTime: Long = 0,
)

data class CommonMessageListBean(
    val last: Boolean = false,
    val totalPages: Int = 0,
    val totalElements: Int = 0,
    val first: Boolean = false,
    val numberOfElements: Int = 0,
    val size: Int = 0,
    val number: Int = 0,
    val page: Int = 0,
    val content: List<CommonMsgContentBean> = listOf(),
    val sort: List<CommonMsgSortBean> = listOf(),
)

data class CommonMsgContentBean(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val pushTime: Long = 0,
    val pushAppOpenType: String = "",
    val openContent: String = "",
    val read: Boolean = false,
    val image: String = "",
)

data class CommonMsgSortBean(
    val direction: String = "",
    val property: String = "",
    val ignoreCase: Boolean = false,
    val nullHandling: String = "",
    val ascending: Boolean = false,
    val descending: Boolean = false,
)

data class CommonThirdPlatformBean(
    val last: Boolean = false,
    val totalPages: Int = 0,
    val totalElements: Int = 0,
    val numberOfElements: Int = 0,
    val size: Int = 0,
    val number: Int = 0,
    val page: Int = 0,
    val first: Boolean = false,
    val content: List<String> = listOf(),
)