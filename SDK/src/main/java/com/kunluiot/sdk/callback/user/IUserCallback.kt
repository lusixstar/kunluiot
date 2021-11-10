package com.kunluiot.sdk.callback.user

import com.kunluiot.sdk.bean.user.AvatarBean
import com.kunluiot.sdk.bean.user.User

interface ILoginCallback {

    fun onSuccess(user: User)

    fun onError(code: String, error: String)
}

interface IUserCallback {

    fun onSuccess(user: User)

    fun onError(code: String, error: String)
}

interface IAvatarCallback {

    fun onSuccess(avatar: AvatarBean)

    fun onError(code: String, error: String)
}
