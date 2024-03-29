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
package com.kunluiot.sdk.thirdlib.kalle.connect;

import android.text.TextUtils;

import com.kunluiot.sdk.thirdlib.kalle.Headers;
import com.kunluiot.sdk.thirdlib.kalle.ResponseBody;
import com.kunluiot.sdk.thirdlib.kalle.util.IOUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Zhenjie Yan on 2018/2/22.
 */
public class StreamBody implements ResponseBody {

    private String mContentType;
    private InputStream mStream;

    public StreamBody(String contentType, InputStream stream) {
        this.mContentType = contentType;
        this.mStream = stream;
    }

    @Override
    public String string() throws IOException {
        String charset = Headers.parseSubValue(mContentType, "charset", null);
        return TextUtils.isEmpty(charset) ? IOUtils.toString(mStream) : IOUtils.toString(mStream, charset);
    }

    @Override
    public byte[] byteArray() throws IOException {
        return IOUtils.toByteArray(mStream);
    }

    @Override
    public InputStream stream() throws IOException {
        return mStream;
    }

    @Override
    public void close() throws IOException {
        mStream.close();
    }
}