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
package com.kunluiot.sdk.thirdlib.kalle.download;

import com.kunluiot.sdk.thirdlib.kalle.Response;
import com.kunluiot.sdk.thirdlib.kalle.connect.http.Call;

import java.io.IOException;

/**
 * Created by Zhenjie Yan on 2018/3/18.
 */
public class BodyWorker extends BasicWorker<BodyDownload> {

    private Call mCall;

    BodyWorker(BodyDownload download) {
        super(download);
    }

    @Override
    protected Response requestNetwork(BodyDownload download) throws IOException {
        mCall = new Call(download);
        return mCall.execute();
    }

    @Override
    public void cancel() {
        if (mCall != null && !mCall.isCanceled()) {
            mCall.asyncCancel();
        }
    }
}