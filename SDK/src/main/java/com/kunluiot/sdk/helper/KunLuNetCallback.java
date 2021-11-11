/*
 * Copyright © 2018 Zhenjie Yan.
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
package com.kunluiot.sdk.helper;

import android.content.Context;

import com.kunluiot.sdk.R;
import com.kunluiot.sdk.kalle.exception.ConnectTimeoutError;
import com.kunluiot.sdk.kalle.exception.HostError;
import com.kunluiot.sdk.kalle.exception.NetworkError;
import com.kunluiot.sdk.kalle.exception.ReadTimeoutError;
import com.kunluiot.sdk.kalle.exception.URLError;
import com.kunluiot.sdk.kalle.exception.WriteException;
import com.kunluiot.sdk.kalle.simple.Callback;
import com.kunluiot.sdk.kalle.simple.SimpleResponse;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

abstract class KunLuNetCallback<S> extends Callback<S, String> {

    private Context mContext;

    public KunLuNetCallback(Context context) {
        this.mContext = context;
    }

    @Override
    public Type getSucceed() {
        Type superClass = getClass().getGenericSuperclass();
        return ((ParameterizedType) superClass).getActualTypeArguments()[0];
    }

    @Override
    public Type getFailed() {
        return String.class;
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onException(Exception e) {
        String message;
        if (e instanceof NetworkError) {
            message = mContext.getString(R.string.http_exception_network);
        } else if (e instanceof URLError) {
            message = mContext.getString(R.string.http_exception_url);
        } else if (e instanceof HostError) {
            message = mContext.getString(R.string.http_exception_host);
        } else if (e instanceof ConnectTimeoutError) {
            message = mContext.getString(R.string.http_exception_connect_timeout);
        } else if (e instanceof WriteException) {
            message = mContext.getString(R.string.http_exception_write);
        } else if (e instanceof ReadTimeoutError) {
            message = mContext.getString(R.string.http_exception_read_timeout);
        } else {
            message = mContext.getString(R.string.http_exception_unknow_error);
        }
        onResponse(SimpleResponse.<S, String>newBuilder().failed(message).build());
    }

    @Override
    public void onCancel() {
    }

    @Override
    public void onEnd() {
    }
}