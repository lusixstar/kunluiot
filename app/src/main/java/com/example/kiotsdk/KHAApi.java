package com.example.kiotsdk;


import com.kunluiot.sdk.thirdlib.ws.websocket.response.ErrorResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;


/**
 * Created by LianCP on 2019/11/26.
 */

public interface KHAApi {


//
//    //微信登录接口
//    @POST(HttpConfig.KHA_API_WE_CHAT_LOGIN)
//    Observable<LoginResponse> weChatLogin(@Body WeChatLoginRequestInfo loginInfo);//传入的参数为RequestBody
//
//    //设备列表接口
//    @GET(HttpConfig.KHA_API_DEVICE)
//    Observable<ArrayList<DevicesResponse>> getDevice();
//
//    //获取摄像头固件
//    @GET(HttpConfig.KHA_API_GET_CAMERA_VERSION)
//    Observable<CameraVersionResp> getCameraVersion(@Query("deviceId") String deviceId);
//
//    //升级摄像头固件
//    @POST(HttpConfig.KHA_API_UPDATE_CAMERA_FIRMWARE)
//    Observable<BaseResp> updateCameraFirmware(@Body RequestBody route);
//
//    //获取摄像头固件结果
//    @GET(HttpConfig.KHA_API_UPDATE_CAMERA_FIRMWARE_PROGRESS)
//    Observable<CameraUpdateCallbackResp> updateCameraFirmwareCallback(@Query("deviceId") String deviceId);
//
//    //扫描二维码调用的设备列表接口
//    @POST(HttpConfig.KHA_API_SCAN_VIRTUAL_CODE_DEVICE)
//    Observable<DevicesResponse> scanVirtualCodeDevice(@Query("reverseTemplateId") String reverseTemplateId);
//
//    //扫描二维码调用的设备列表接口
//    @POST(HttpConfig.KHA_API_SCAN_UN_VIRTUAL_CODE_DEVICE)
//    Observable<ErrorResponse> scanUnVirtualCodeDevice(@Query("reverseTemplateId") String reverseTemplateId);

//    //每隔3秒轮询查询更换WiFi状态
//    @GET(HttpConfig.KHA_API_DEVICE_POLLING_WIFI)
//    Observable<PollingWifiResponse> pollingDeviceWifi(@Path("ctrlKey") String ctrlKey);








//
//    //编辑联动场景
//    @PUT(HttpConfig.KHA_API_LINKAGE_SCENE_LIST)
//    Observable<ErrorResponse> updateLinkageScene(@Query("ruleId") String ruleId, @Query("enable") boolean enable, @Body RequestBody body);
//
//    //删除联动场景
//    @DELETE(HttpConfig.KHA_API_DELETE_LINKAGE_SCENE)
//    Observable<DeleteLinkageSceneResponse> deleteLinkageScene(@Query("ruleId") String ruleId);
//
//    //删除已使用的联动场景
//    @DELETE(HttpConfig.KHA_API_DELETE_LINKAGE_SCENE)
//    Observable<ErrorResponse> deleteUseLinkageScene(@Query("ruleId") String ruleId, @Query("randomToken") String randomToken);
//
//    //新玩法列表接口
//    @GET(HttpConfig.KHA_API_NEW_PLAY)
//    Observable<NewPlayBean> getNewPlay(@Query("pid") String pid);
//
//    //更新一键场景排序
//    @PATCH(HttpConfig.KHA_API_UPDATE_ONEKEY_SCENE_SORT)
//    Observable<Map> updateOneKeySort(@Body RequestBody body);
//
//    //更新联动场景排序
//    @PATCH(HttpConfig.KHA_API_UPDATE_LINK_SCENE_SORT)
//    Observable<Map> updateLinkSceneSort(@Body RequestBody body);
//
//
//    //常见问题列表接口
//    @GET(HttpConfig.KHA_API_COMMON_PROBLEM)
//    Observable<CommonProblemResponse> getCommonProblem(@Query("pid") String pid);
//
//    //检测APK版本接口
//    @GET(HttpConfig.KHA_API_CHECK_APK_VERSION)
//    Observable<UpgradeApkInfoResponse> checkAppVersion(@Query("pid") String pid, @Query("platform") int platform);
//
//    //意见反馈
//    @POST(HttpConfig.KHA_API_FEEDBACK)
//    Observable<StateResult> feedback(@Body RequestBody route);
//
//    //获取天气接口
//    @GET(HttpConfig.KHA_API_NOW_WEATHER)
//    Observable<NowWeatherResponse> getNowWeather(@Query("location") String location, @Query("timestamp") long timestamp,
//            @Query("sign") String sign, @Query("language") String language);
//
//    //平台消息列表接口
//    @GET(HttpConfig.KHA_API_MESSAGE_PLATFORM)
//    Observable<MessagePlatformResponse> getMessagePlatform(@Query("page") int page, @Query("size") int size);
//
//    //设备消息列表接口
//    @GET(HttpConfig.KHA_API_MESSAGE_DEVICE)
//    Observable<MessageDevicesResponse> getMessageDevice(@Query("page") int page, @Query("size") int size);
//
//    //设备消息置为已读接口
//    @PATCH(HttpConfig.KHA_API_MESSAGE_DEVICE_READ)
//    Observable<ErrorResponse> readMessageDevice(@Path("id") String id);
//
//    //设备消息全部置为已读接口
//    @PATCH(HttpConfig.KHA_MESSAGE_DEVICE_ALL_READ)
//    Observable<ErrorResponse> allReadMessageDevice();
//
//    //设备消息清空接口
//    @DELETE(HttpConfig.KHA_API_MESSAGE_DEVICE_EMPTY)
//    Observable<ErrorResponse> emptyMessageDevice();
//
//    //绑定的第三方平台列表接口
//    @GET(HttpConfig.KHA_API_BIND_THIRD_PLATFORM_LIST)
//    Observable<ThirdPlatformResponse> getBindThirdPlatformList(@Query("pid") String pid);
//
//    //第三方平台列表接口
//    @GET(HttpConfig.KHA_API_THIRD_PLATFORM_LIST)
//    Observable<ThirdPlatformResponse> getThirdPlatformList(@Query("pid") String pid);
//
//    //绑定推送
//    @POST(HttpConfig.KHA_API_BIND_PUSH)
//    Observable<ErrorResponse> bindPush(@Body RequestBody route);
//
//    //解绑推送
//    @POST(HttpConfig.KHA_API_UN_BIND_PUSH)
//    Observable<ErrorResponse> unBindPush(@Body RequestBody route);
//
//    //商城列表接口
//    @GET(HttpConfig.KHA_API_MALLS_LIST)
//    Observable<ArrayList<MallsBean>> getMallsList(@Query("pid") String pid, @Query("type") String type);
//
//
//    //获取可绑定的摄像头产品
//    @GET(HttpConfig.KHA_API_CAMERA_ENABLE_BIND_LIST)
//    Observable<EnableCameraListResp> getEnableCameraList(@Query("pid") String pid);
//
//    //获取可绑定的摄像头产品
//    @GET(HttpConfig.THIRD_API_CAMERA_BIND_STATUS)
//    Observable<CameraBindStatusResp> getCameraBindStatus(@Query("user_id") String userId, @Header("Authorization") String token);
//
//    //获取万佳安token
//    @GET(HttpConfig.THIRD_API_CAMERA_REQ_TOKEN)
//    Observable<WorthCloudAuthorization> getWorthCloudToken();
//
//    //万佳安平台绑定
//    @POST(HttpConfig.THIRD_API_WORTHCLOUD_BIND_CAMERA)
//    Observable<WorthCloudEmptyResp> bindWorthCamera(@Query("user_id") String userId, @Query("device_id") String deviceId,
//            @Header("Authorization") String token);
//
//    //绑定摄像头
//    @POST(HttpConfig.KHA_API_BIND_CAMERA)
//    Observable<BaseResp> bindCamera(@Body RequestBody route);
//
//    //获取已绑定摄像头列表
//    @GET(HttpConfig.KHA_API_GET_CAMERALIST)
//    Observable<CameraListResp> getBindedCameraList(@Query("accountId") String accountId, @Query("family") String family);
//
//    //转动摄像头
//    @POST(HttpConfig.KHA_API_DIRECT_CAMERA)
//    Observable<ArrayList<BindedCamera>> directCamera(@Body RequestBody body);
//
//    //解绑摄像头
//    @POST(HttpConfig.KHA_API_UNBIND_CAMERA)
//    Observable<BaseResp> unbindCamera(@Body RequestBody body);
//
//    //获取摄像头详情
//    @GET(HttpConfig.KHA_API_GET_CAMERA_INFO)
//    Observable<DeviceInfoResp> getCameraInfo(@Query("deviceId") String deviceId);
//
//    //修改摄像头信息
//    @POST(HttpConfig.KHA_API_EDIT_CAMERA_INFO)
//    Observable<BaseResp> editCamera(@Body RequestBody body);
//
//    //获取告警信息
//    @GET(HttpConfig.KHA_API_GET_ALERT_MSG)
//    Observable<AlertMsgResp> getAlertMessage(@Query("deviceId") String deviceId, @Query("page") int page,
//            @Query("size") int size, @Query("type") String type, @Query("alarmDate") String alarmDate);
//
//    //获取告警信息设置
//    @GET(HttpConfig.KHA_API_GET_ALERT_SETTINGS)
//    Observable<AlertSettingsResp> getAlertSettings(@Query("deviceId") String deviceId);
//
//    //设置摄像头家庭告警
//    @POST(HttpConfig.KHA_API_EDIT_ALERT_SETTINGS)
//    Observable<BaseResp> editAlertSettings(@Body RequestBody body);
//
//    //切换万佳安清晰度
//    @POST(HttpConfig.KHA_API_EDIT_DEFINITION)
//    Observable<BaseResp> changeDefinition(@Body RequestBody body);
//
//    //获取摄像头音量
//    @GET(HttpConfig.KHA_API_GET_CAMERA_VOLUME)
//    Observable<CameraVolume> getCameraVolume(@Query("deviceId") String deviceId);
//
//    //设置摄像头音量
//    @POST(HttpConfig.KHA_API_SET_CAMERA_VOLUME)
//    Observable<BaseResp> changeCameraVolume(@Body RequestBody body);
//
//    //获取摄像头移动追踪
//    @GET(HttpConfig.KHA_API_GET_CAMERA_FOLLOW)
//    Observable<CameraVolume> getCameraFollow(@Query("deviceId") String deviceId);
//
//    //设置摄像头移动追踪
//    @POST(HttpConfig.KHA_API_SET_CAMERA_FOLLOW)
//    Observable<BaseResp> changeCameraFollow(@Body RequestBody body);
//
//    //编辑子设备名称
//    @PATCH(HttpConfig.KHA_API_SET_SUB_DEVICE_NAME)
//    Observable<ErrorResponse> editSubDeviceName(@Path("parentDevTid") String parentDevTid, @Path("devTid") String devTid, @Body RequestBody body); //编辑子设备名称
//
//    @PATCH(HttpConfig.KHA_API_SET_DEVICE_NAME)
//    Observable<ErrorResponse> editDeviceName(@Path("devTid") String devTid, @Body RequestBody body);
//
//    //获取未读消息
//    @GET(HttpConfig.KHA_API_GET_UNREAD_STATUS)
//    Observable<String> getUnreadMsg();
//
//    //绑定音响
//    @POST(HttpConfig.KHA_API_BIND_SOUND)
//    Observable<StateResult<BindSoundBean>> bindSound(@Body RequestBody route);
//
//    //用户映射
//    @POST(HttpConfig.KHA_API_SPEECH_USERID)
//    Observable<StateResult> speechUid(@Query("speechUserId") String speechUserId);
//
//    //改变消息已读
//    @PUT(HttpConfig.KHA_API_READ_MSG_FOLDER)
//    Observable<String> readMsg(@Path("id") String id);
//
//    //清空
//    @PUT(HttpConfig.KHA_API_CLEAR_MSG)
//    Observable<String> clearMsg();
//
//    //获取设备授权用户数
//    @GET(HttpConfig.KHA_API_GET_DEVICE_AUTH_COUNT)
//    Observable<String> getDeviceAuthCount(@Query("ctrlKey") String ctrlKey);
//
//    //音箱设备列表
//    @GET(HttpConfig.KHA_API_SOUND_LIST)
//    Observable<SoundListBean> getDeviceList();
//
//    //音箱是否绑定
//    @GET(HttpConfig.KHA_API_SOUND_IS_BIND)
//    Observable<SoundIsBind> judgeSoundIsBind(@Query("pid") String pid);
//
//    //获取所有zigbeen页面和协议
//    @GET(HttpConfig.KHA_API_GET_ALL_ZIGBEEN)
//    Observable<H5PageResp> getAllZigbeen();
//
//    //检查协调器版本
//    @POST(HttpConfig.KHA_API_CHECK_ZIG_VER)
//    Observable<List<DeviceCheckUpdateBean>> checkZigVer(@Body RequestBody route);
//
//    //同步离线添加的设备
//    @POST(HttpConfig.KHA_API_ASYNC_DEVICE)
//    Observable<Map> asyncDevice(@Body RequestBody route);
//
//
//    //同步离线添加的设备
//    @POST(HttpConfig.KHA_API_ASYNC_BLUETOOTH_DEVICE)
//    Observable<Map> asyncBluetoothDevice(@Body RequestBody route);
//
//    //绑定大华摄像头
//    @POST(HttpConfig.KHA_API_BIND_DAHUA_CAMERA)
//    Observable<StateResult> bindDaHuaCamera(@Body RequestBody body);
//
//    //解绑大华摄像头
//    @POST(HttpConfig.KHA_API_UNBIND_DAHUA_CAMERA)
//    Observable<StateResult> unBindDaHuaCamera(@Body RequestBody body);
//
//    //大华摄像头详情
//    @GET(HttpConfig.KHA_API_DAHUA_CAMERA_INFO)
//    Observable<StateResult<LcCameraInfoBean>> getLCCameraInfo(@Query("deviceId") String deviceId);
//
//    //旋转大华摄像头
//    @POST(HttpConfig.KHA_API_DAHUA_CAMERA_ROTATE)
//    Observable<StateResult> rotateDaHuaCamera(@Body RequestBody body);
//
//    //获取设备SD卡状态
//    @GET(HttpConfig.KHA_API_DAHUA_CAMERA_SDCARD)
//    Observable<StateResult<SDCardStatus>> getSDCardStatus(@Query("deviceId") String deviceId);
//
//    //设备SD卡格式化
//    @POST(HttpConfig.KHA_API_DAHUA_CAMERA_RECOVER_SDCARD)
//    Observable<StateResult> formatSDCard(@Body RequestBody body);
//
//    // 获取设备存储介质容量信息
//    @GET(HttpConfig.KHA_API_DAHUA_CAMERA_STORAGE)
//    Observable<StateResult<StorageBean>> getLCStorage(@Query("deviceId") String deviceId);
//
//    //获取布防时间
//    @GET(HttpConfig.KHA_API_DAHUA_CAMERA_DEPTIME)
//    Observable<StateResult<LCDepTime>> getDepTime(@Query("deviceId") String deviceId);
//
//    //设置布防时间
//    @POST(HttpConfig.KHA_API_DAHUA_CAMERA_SET_DEPTIME)
//    Observable<StateResult> setDepTime(@Body RequestBody body);
//
//    //获取摄像头的设置信息
//    @GET(HttpConfig.KHA_API_DAHUA_CAMERA_GET_SETINFO)
//    Observable<StateResult<LCSetBean>> getLCSetting(@Query("deviceId") String deviceId);
//
//    //设置摄像头的设置信息
//    @POST(HttpConfig.KHA_API_DAHUA_CAMERA_SET_SETINFO)
//    Observable<StateResult> setLCSetting(@Body RequestBody body);
//
//    //控制设备连接热点(wifi切换)
//    @POST(HttpConfig.KHA_API_DAHUA_CAMERA_CHANGEWIFI)
//    Observable<StateResult> changeLCWifi(@Body RequestBody body);
//
//    //告警信息
//    @GET(HttpConfig.KHA_API_DAHUA_CAMERA_ALERT)
//    Observable<StateResult<AlertBean>> getAlertInfo(@Query("deviceId") String deviceId, @Query("page") int page,
//            @Query("size") int size, @Query("type") String type, @Query("alarmDate") long alarmDate);
//
//    //设备重启
//    @POST(HttpConfig.KHA_API_DAHUA_CAMERA_RESTART)
//    Observable<StateResult> reStartLCCamera(@Body RequestBody body);
//
//    //设备转移
//    @POST(HttpConfig.KHA_API_DAHUA_CAMERA_TRANSFER)
//    Observable<StateResult> transferLCCamera(@Body RequestBody body);
//
//    //乐橙固件升级
//    @POST(HttpConfig.KHA_API_DAHUA_CAMERA_UPDATE_VERSION)
//    Observable<StateResult> updateLCCamera(@Body RequestBody body);
//
//    //获取动检区域
//    @GET(HttpConfig.KHA_API_DAHUA_CAMERA_GET_ALARMREGION)
//    Observable<AlarmRegionBean> getAlarmRegion(@Query("deviceId") String deviceId);
//
//    //设置动检区域
//    @POST(HttpConfig.KHA_API_DAHUA_CAMERA_SET_ALARMREGION)
//    Observable<StateResult> setAlarmRegion(@Body RequestBody body);
//
//    //检测手机号
//    @POST(HttpConfig.KHA_API_DAHUA_CHECKPHONE_ISREGISTER)
//    Observable<StateResult<CheckPhone>> checkPhone(@Body RequestBody body);
//
//    //乐橙摄像头固件是否可升级
//    @GET(HttpConfig.KHA_API_DAHUA_CAMERA_CAN_UPDATE)
//    Observable<StateResult<LCCameraUpdate>> canUpdateLC(@Query("deviceId") String deviceId);
//
//    //乐橙摄像头固件升级
//    @POST(HttpConfig.KHA_API_DAHUA_CAMERA_UPDATE)
//    Observable<StateResult> updateLC(@Body RequestBody body);
//
//    //音箱获取场景
//    @GET(HttpConfig.KHA_API_SOUND_GET_SCENE)
//    Observable<GetSceneBean> getSoundScene();
//
//    //音箱新增场景
//    @POST(HttpConfig.KHA_API_SOUND_ADD_SCENE)
//    Observable<StateResult> addSoundScene(@Body RequestBody body);
//
//    //音箱删除场景
//    @DELETE(HttpConfig.KHA_API_SOUND_DELETE_SCENE)
//    Observable<StateResult> deleteSoundScene(@Query("id") String id);
//
//    //获取格式化状态
//    @GET(HttpConfig.KHA_API_GET_TF_FORMAT)
//    Observable<StateResult<FormatStatusBean>> getTfFormat(@Query("deviceId") String deviceId);
//
//    //格式化摄像头TF卡
//    @POST(HttpConfig.KHA_API_SET_TF_FORMAT)
//    Observable<StateResult> setTfFormat(@Body RequestBody body);
//
//    //获取休眠状态
//    @GET(HttpConfig.KHA_API_GET_SLEEP_SWITCH)
//    Observable<StateResult<CameraSleepBean>> getSleepSwitch(@Query("deviceId") String deviceId);
//
//    //设置休眠状态
//    @POST(HttpConfig.KHA_API_SET_SLEEP_SWITCH)
//    Observable<StateResult> setSleepSwitch(@Body RequestBody body);
//
//    //获取指示灯开关
//    @GET(HttpConfig.KHA_API_GET_LIGHT_SWITCH)
//    Observable<StateResult<LightSwitchBean>> getLightSwitch(@Query("deviceId") String deviceId);
//
//    //设置指示灯开关
//    @POST(HttpConfig.KHA_API_SET_LIGHT_SWITCH)
//    Observable<StateResult> setLightSwitch(@Body RequestBody body);
//
//    //获取摄像头翻转
//    @GET(HttpConfig.KHA_API_GET_VIDEO_DIRECTION)
//    Observable<StateResult<VideoDirectionBean>> getVideoDirection(@Query("deviceId") String deviceId);
//
//    //设置摄像头翻转
//    @POST(HttpConfig.KHA_API_SET_VIDEO_DIRECTION)
//    Observable<StateResult> setVideoDirection(@Body RequestBody body);
//
//    //获取预设情景面包
//    @GET(HttpConfig.KHA_API_GET_SCENE_TEMPLATE)
//    Observable<List<SceneOneKeyResponse>> getSceneTemplate();
//
//    //获取可绑定的摄像头产品
//    @GET(HttpConfig.THIRD_API_CAMERA_DEVICE_STATUS)
//    Observable<Map> getCameraInfo(@Query("device_id") String userId, @Header("Authorization") String token);
//
//    //raw解密
//    @POST(HttpConfig.KHA_API_DECODE)
//    Observable<StateResult> decodeRaw(@Body RequestBody body);
//
//    //raw加密
//    @POST(HttpConfig.KHA_API_ENCODE)
//    Observable<StateResult> encodeRaw(@Body RequestBody body);
//
//    //检测MCU版本
//    @GET(HttpConfig.KHA_API_MCU_VER)
//    Observable<List<McuInfo>> checkMcuVer(@Query("ctrlKeys") String ctrlKeys);
//
//    //绑定的第三方平台列表接口
//    @POST(HttpConfig.KHA_API_GET_ANOTHER_NAME)
//    Observable<Map> getAnotherName(@Body RequestBody body);
//
//    //获取说明书
//    @GET(HttpConfig.INSTRUCTION_LIST)
//    Observable<ArrayList<DeviceProductResponse>> getProductIntro();
//
//    //获取说明书
//    @POST(HttpConfig.KHA_API_BIND_BLUETOOTH_DEVICE)
//    Observable<Map> bindBluetoothDevice(@Body RequestBody body);
//    //获取蓝牙设备json
//    @GET(HttpConfig.KHA_API_GET_BLUETOOTH_DEVICE)
//    Observable<List<DevicesResponse>> getBluetoothDevice(@Query("bluetoothId") String bluetoothId, @Query("owner") Boolean owner);
//
//    @GET(HttpConfig.KHA_API_GET_BLUETOOTH_DEVICE_GROUP)
//    Observable<List<BluetoothGroupBean>> getBluetoothGroup(@Query("groupId") String groupId);
//
//    @DELETE(HttpConfig.KHA_API_GET_BLUETOOTH_DEVICE_GROUP)
//    Observable<Map> deleteBluetoothGroup(@Query("groupId") String groupId);
//
//    @POST(HttpConfig.KHA_API_SEND_BLUETOOTH_CONFIG)
//    Observable<Map> sendBluetoothConfig(@Body RequestBody body);
//
//    //所有设备
//    @GET(HttpConfig.KHA_API_DEVICES)
//    Observable<ArrayList<BluetoothDevicesResponse>> getAllBluetoothDevices(@Query("bluetooth") boolean bluetooth
//            ,@Query("owner") boolean owner,@Query("cid") String cid);
//
//    @POST(HttpConfig.KHA_API_GET_BLUETOOTH_DEVICE_GROUP)
//    Observable<List<BluetoothGroupBean>> createBluetoothGroup(@Body RequestBody body);
//
//    @GET(HttpConfig.KHA_API_SEND_BLUETOOTH_CONFIG)
//    Observable<Map> getBluetoothConfig();
//
//    @POST(HttpConfig.KHA_API_REMOVE_BLUETOOTH_FROM_GROUP)
//    Observable<Map> removeBluetoothDevFromGroup(@Path("groupId") String groupId,@Body RequestBody body);
//
//    @POST(HttpConfig.KHA_API_ADD_BLUETOOTH_DEVICE_TO_GROUP)
//    Observable<Map> addBluetoothDev2Group(@Path("groupId") String groupId,@Body RequestBody body);
//
//    @PUT(HttpConfig.KHA_API_ADD_BLUETOOTH_DEVICE_TO_GROUP)
//    Observable<Map> editBluetoothGroupName(@Path("groupId") String groupId,@Body RequestBody body);
//
//    @PUT(HttpConfig.KHA_API_EDIT_BLUETOOTH_GROUP_ADDRESS)
//    Observable<Map> editBluetoothGroupAddress(@Path("groupId") String groupId,@Body RequestBody body);
//
//    @POST(HttpConfig.KHA_API_SEQUENCENUMBER_SETTINGS)
//    Observable<Map> setSequenceNumber(@Body RequestBody body);
//
//    @GET(HttpConfig.KHA_API_SEQUENCENUMBER_SETTINGS)
//    Observable<Map> getSequenceNumber();
//
//    @GET(HttpConfig.KHA_API_DEVICES)
//    Observable<List<DevicesResponse>> getBluetoothDevice(@Query("bluetooth") boolean bluetooth,@Query("owner") boolean owner);
//
//    @PATCH(HttpConfig.KHA_API_UPDATE_MESH_ADDRESS)
//    Observable<Map> updateMeshAddress(@Body RequestBody body);
//
//    @GET(HttpConfig.KHA_API_GET_MID2PRO)
//    Observable<Map> getBluetoothMidPro();
//
//    @POST(HttpConfig.KHA_API_CREATE_BLUEGROUPS)
//    Observable<Map> createBluetoothGroups(@Body RequestBody body);
//
//    @GET(HttpConfig.KHA_API_GET_BLUETOOTH_SETTINGS)
//    Observable<Map> getBluetoothSettings();
//
//    @POST(HttpConfig.KHA_API_GET_BLUETOOTH_PRODUCTS)
//    Observable<List<BluetoothProBean>> getBluetoothProducts(@Path("mid") String mid, @Body RequestBody body);
//
//    @PATCH(HttpConfig.KHA_API_DEVICE_BATCH_EDIT)
//    Observable<Map> deviceBathEdit(@Body RequestBody body);
//
//    @POST(HttpConfig.KHA_API_GET_PRO_INFO)
//    Observable<List<BleDiscoverBean>> getProductInfo(@Body RequestBody body);
}
