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
package com.kunluiot.sdk.thirdlib.kalle;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Zhenjie Yan on 2018/2/22.
 */
public interface ResponseBody extends Closeable {

    /**
     * Transform the response data into a string.
     */
    String string() throws IOException;

    /**
     * Transform the response data into a byte array.
     */
    byte[] byteArray() throws IOException;

    /**
     * Transform the response data into a stream.
     */
    InputStream stream() throws IOException;
}