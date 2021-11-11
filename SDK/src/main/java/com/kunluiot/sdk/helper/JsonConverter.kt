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
package com.kunluiot.sdk.helper

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kunluiot.sdk.BuildConfig
import com.kunluiot.sdk.R
import com.kunluiot.sdk.bean.common.BaseRespBean
import com.kunluiot.sdk.kalle.Response
import com.kunluiot.sdk.kalle.simple.Converter
import com.kunluiot.sdk.kalle.simple.SimpleResponse
import com.kunluiot.sdk.log.KunLuLog
import java.lang.reflect.Type

class JsonConverter(private val mContext: Context) : Converter {

    private val gson = GsonBuilder().create()

    @Throws(Exception::class)
    override fun <S, F> convert(succeed: Type, failed: Type, response: Response, fromCache: Boolean): SimpleResponse<S, F> {
        var succeedData: S? = null // The data when the business successful.
        var failedData: F? = null // The data when the business failed.
        val code = response.code()
        val serverJson = response.body().string()

        if (BuildConfig.DEBUG) KunLuLog.i("Server Code: $code")
        if (BuildConfig.DEBUG) KunLuLog.i("Server Data: $serverJson")

        when {
            code in 200..299 -> { // Http is successful.
                try {
                    val data: S = gson.fromJson(response.body().string(), succeed)
                    succeedData = data
                } catch (e: Exception) {
                    failedData = mContext.getString(R.string.http_server_data_format_error) as F
                }
            }
            code in 400..499 -> {
                try {
                    val data = gson.fromJson(response.body().string(), BaseRespBean::class.java)
                    data.status = data.code
                    data.message = data.desc
                    succeedData = data as S
                } catch (e: Exception) {
                    failedData = mContext.getString(R.string.http_unknow_error) as F
                }
            }
            code >= 500 -> {
                failedData = mContext.getString(R.string.http_server_error) as F
            }
        }

        return SimpleResponse.newBuilder<S, F>().code(response.code()).headers(response.headers()).fromCache(fromCache).succeed(succeedData).failed(failedData).build()
    }
}