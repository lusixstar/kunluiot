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
package com.kunluiot.sdk.kalle.connect.http;

import com.kunluiot.sdk.kalle.BodyRequest;
import com.kunluiot.sdk.kalle.Headers;
import com.kunluiot.sdk.kalle.Request;
import com.kunluiot.sdk.kalle.RequestMethod;
import com.kunluiot.sdk.kalle.Response;
import com.kunluiot.sdk.kalle.Url;
import com.kunluiot.sdk.kalle.UrlRequest;
import com.kunluiot.sdk.kalle.connect.Interceptor;
import com.kunluiot.sdk.kalle.util.IOUtils;

import java.io.IOException;

import static com.kunluiot.sdk.kalle.Headers.KEY_COOKIE;

/**
 * Created by Zhenjie Yan on 2018/3/6.
 */
public class RedirectInterceptor implements Interceptor {

    public RedirectInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        if (response.isRedirect()) {
            Url oldUrl = request.url();
            Url url = oldUrl.location(response.headers().getLocation());
            Headers headers = request.headers();
            headers.remove(KEY_COOKIE);

            RequestMethod method = request.method();
            Request newRequest;
            if (method.allowBody()) {
                newRequest = BodyRequest.newBuilder(url, method)
                        .setHeaders(headers)
                        .setParams(request.copyParams())
                        .body(request.body())
                        .build();
            } else {
                newRequest = UrlRequest.newBuilder(url, method)
                        .setHeaders(headers)
                        .build();
            }
            IOUtils.closeQuietly(response);
            return chain.proceed(newRequest);
        }
        return response;
    }
}