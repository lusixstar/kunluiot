package com.example.kiotsdk.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.jetbrains.anko.toast

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    fun toastErrorMsg(code: String, msg: String) {
        toast("code == $code, error == $msg")
    }

    fun toastMsg(msg: String) {
        toast(msg)
    }
}