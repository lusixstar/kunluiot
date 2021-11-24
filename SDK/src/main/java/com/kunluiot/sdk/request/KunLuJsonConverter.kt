/*
 * Copyright 2018 Zhenjie Yan.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kunluiot.sdk.request

import android.content.Context
import com.kunluiot.sdk.BuildConfig
import com.kunluiot.sdk.R
import com.kunluiot.sdk.thirdlib.kalle.Response
import com.kunluiot.sdk.thirdlib.kalle.simple.Converter
import com.kunluiot.sdk.thirdlib.kalle.simple.SimpleResponse
import com.kunluiot.sdk.util.log.KunLuLog
import org.json.JSONObject
import java.lang.reflect.Type

class KunLuJsonConverter(private val mContext: Context) : Converter {

    @Throws(Exception::class)
    override fun <S, F> convert(succeed: Type, failed: Type, response: Response, fromCache: Boolean): SimpleResponse<S, F> {
        var succeedData: S? = null // The data when the business successful.
        var failedData: F? = null // The data when the business failed.
        var code = response.code()
        val serverJson = response.body().string()

        if (BuildConfig.DEBUG) KunLuLog.i("Server Code: $code")
        if (BuildConfig.DEBUG) KunLuLog.i("Server Data: $serverJson")

        when (code) {
            in 200..299 -> { // Http is successful.
                try {
                    var oStatus = 0
                    var oMsg = ""
                    var oDesc = ""
                    var oCode = 0
                    if (response.body().string().startsWith("[")) {
                        succeedData = response.body().string() as S
                    } else if (response.body().string().isEmpty()) {
                        succeedData = "" as S
                    } else {
                        val obj = JSONObject(response.body().string())
                        if (obj.has("status")) oStatus = obj.getInt("status")
                        if (obj.has("message")) oMsg = obj.getString("message")
                        if (obj.has("desc")) {
                            oDesc = obj.getString("desc")
                            oMsg = oDesc
                        }
                        if (obj.has("code")) {
                            oCode = obj.getInt("code")
                            oStatus = oCode
                        }
                        if (obj.has("data")) {
                            if (oStatus == 200) {
                                succeedData = if (obj.getString("data").toString() != "null")  {
                                    obj.getString("data").toString() as S
                                } else {
                                    "" as S
                                }
                            } else {
                                code = oStatus
                                failedData = oMsg as F
                            }
                        } else {
                            if (oCode == 200 || oCode == 0) {
                                succeedData = response.body().string() as S
                            } else {
                                code = oCode
                                failedData = oMsg as F
                            }
                        }
                    }
                } catch (e: Exception) {
                    failedData = mContext.getString(R.string.http_server_data_format_error) as F
                }
            }
            in 400..499 -> {
                try {
                    var oStatus = 0
                    var oMsg = ""
                    val obj = JSONObject(response.body().string())
                    if (obj.has("status")) oStatus = obj.getInt("status")
                    if (obj.has("code")) oStatus = obj.getInt("code")
                    if (obj.has("message")) oMsg = obj.getString("message")
                    if (obj.has("desc")) oMsg = obj.getString("desc")
                    if (oStatus != 0) code = oStatus
                    failedData = oMsg as F
                } catch (e: Exception) {
                    failedData = mContext.getString(R.string.http_unknow_error) as F
                }
            }
            in 500..599 -> {
                failedData = mContext.getString(R.string.http_server_error) as F
            }
        }

        return SimpleResponse.newBuilder<S, F>().code(code).headers(response.headers()).fromCache(fromCache).succeed(succeedData).failed(failedData).build()
    }
}