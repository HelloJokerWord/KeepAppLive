package com.example.keepalivedemo.service

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

/**
 * CreateBy:Joker
 * CreateTime:2021/11/25 10:32
 * description：
 */
class ScapegoatService : Service() {

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.i(ForegroundService.TAG, "onCreate 创建前台服务替身")
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        startForeground(ForegroundService.SERVICE_ID, Notification())
        stopForeground(true) //移除通知栏消息
        stopSelf()
        return START_STICKY
    }

    override fun onDestroy() {
        Log.i(ForegroundService.TAG, "onDestroy 销毁前台服务替身")
        super.onDestroy()
    }
}