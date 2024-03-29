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
package com.kunluiot.sdk.thirdlib.kalle.exception;

import com.kunluiot.sdk.thirdlib.kalle.Headers;

/**
 * Created by Zhenjie Yan on 2018/3/18.
 */
public class DownloadError extends ReadException {

    private int mCode;
    private Headers mHeaders;

    public DownloadError(int code, Headers headers, String message) {
        super(message);
        this.mCode = code;
        this.mHeaders = headers;
    }

    public DownloadError(int code, Headers headers, Throwable cause) {
        super(cause);
        this.mCode = code;
        this.mHeaders = headers;
    }

    public int getCode() {
        return mCode;
    }

    public Headers getHeaders() {
        return mHeaders;
    }
}