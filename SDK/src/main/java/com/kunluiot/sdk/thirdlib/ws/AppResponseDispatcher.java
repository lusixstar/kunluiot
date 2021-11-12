package com.kunluiot.sdk.thirdlib.ws;

import com.google.gson.JsonSyntaxException;
import com.kunluiot.sdk.util.JsonUtils;
import com.kunluiot.sdk.thirdlib.ws.websocket.SimpleDispatcher;
import com.kunluiot.sdk.thirdlib.ws.websocket.dispatcher.ResponseDelivery;
import com.kunluiot.sdk.thirdlib.ws.websocket.response.ErrorResponse;
import com.kunluiot.sdk.thirdlib.ws.websocket.response.Response;
import com.kunluiot.sdk.thirdlib.ws.websocket.response.ResponseFactory;


import java.util.Map;

public class AppResponseDispatcher extends SimpleDispatcher {

    /**
     * JSON 数据格式错误
     */
    public static final int JSON_ERROR = 11;
    /**
     * code 码错误
     */
    public static final int CODE_ERROR = 12;

    @Override
    public void onMessage(String message, ResponseDelivery delivery) {


        try {
            CommonResponseEntity response = JsonUtils.fromJson(message, CommonResponseEntity.class);
            if (response!=null&&(response.getCode() == 200 || response.getMsgId() > -1)) {
                delivery.onMessage(message, response);
            } else {
                ErrorResponse errorResponse = ResponseFactory.createErrorResponse();
                errorResponse.setErrorCode(CODE_ERROR);
                Response<String> textResponse = ResponseFactory.createTextResponse();
                textResponse.setResponseData(message);
                errorResponse.setResponseData(textResponse);
                errorResponse.setReserved(response);
                onSendDataError(errorResponse, delivery);
            }
        }catch (JsonSyntaxException jsonException){
            jsonException.printStackTrace();
            try{
                Map<String,Object> map = JsonUtils.fromJson(message, Map.class);
                delivery.onMessage(message, map);
            }catch (Exception e){
                ErrorResponse errorResponse = ResponseFactory.createErrorResponse();
                Response<String> textResponse = ResponseFactory.createTextResponse();
                textResponse.setResponseData(message);
                errorResponse.setResponseData(textResponse);
                errorResponse.setErrorCode(JSON_ERROR);
                errorResponse.setCause(e);
                onSendDataError(errorResponse, delivery);
            }

        }catch (Exception e) {
            ErrorResponse errorResponse = ResponseFactory.createErrorResponse();
            Response<String> textResponse = ResponseFactory.createTextResponse();
            textResponse.setResponseData(message);
            errorResponse.setResponseData(textResponse);
            errorResponse.setErrorCode(JSON_ERROR);
            errorResponse.setCause(e);
            onSendDataError(errorResponse, delivery);
        }
    }

    /**
     * 统一处理错误信息，
     * 界面上可使用 ErrorResponse#getDescription() 来当做提示语
     */
    @Override
    public void onSendDataError(ErrorResponse error, ResponseDelivery delivery) {
        switch (error.getErrorCode()) {
            case ErrorResponse.ERROR_NO_CONNECT:
//                error.setDescription(MainApplication.getApplication().getString(R.string.network_error));
//                LogUtils.e("AppResponseDispatcher.class", "网络错误");
                break;
            case ErrorResponse.ERROR_UN_INIT:
//                error.setDescription(MainApplication.getApplication().getString(R.string.connection_not_initialized));
//                LogUtils.e("AppResponseDispatcher.class", "连接未初始化");
                break;
            case ErrorResponse.ERROR_UNKNOWN:
//                error.setDescription(MainApplication.getApplication().getString(R.string.unknown_error));
//                LogUtils.e("AppResponseDispatcher.class", "未知错误");
                break;
            case JSON_ERROR:
//                error.setDescription(MainApplication.getApplication().getString(R.string.abnormal_data_format));
//                LogUtils.e("AppResponseDispatcher.class", "数据格式异常");
                break;
            case CODE_ERROR:
//                error.setDescription(MainApplication.getApplication().getString(R.string.response_code_error));
//                LogUtils.e("AppResponseDispatcher.class", "响应码错误");
                break;
        }
        delivery.onSendDataError(error);
    }
}
