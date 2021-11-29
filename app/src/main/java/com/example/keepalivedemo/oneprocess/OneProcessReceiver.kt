package com.example.keepalivedemo.oneprocess

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.keepalivedemo.MainActivity

/**
 * CreateBy:Joker
 * CreateTime:2021/11/29 16:24
 * description：
 */
class OneProcessReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i(OneProcessService.TAG, "收到保活广播")
        context?.let {
            if (!MainActivity.isServiceRunning(it.applicationContext, OneProcessService::class.java)) {
                Log.i(OneProcessService.TAG, "检测到服务未在运行,启动服务")
                val oneProcessService = Intent(it, OneProcessService::class.java)
                it.startService(oneProcessService)
            } else {
                Log.i(OneProcessService.TAG, "检测到服务正在运行,无需再次启动")
            }
        }
    }
}