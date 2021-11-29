package com.example.keepalivedemo

import android.app.Application
import com.example.keepalivedemo.oneprocess.SharedPreferenceTool

/**
 * CreateBy:Joker
 * CreateTime:2021/11/29 16:53
 * description：
 */
class KeepLiveApp : Application() {

    override fun onCreate() {
        super.onCreate()
        SharedPreferenceTool.instance.init(this)
    }
}