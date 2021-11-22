package com.example.keepalivedemo.onepiece

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

/**
 * CreateBy:Joker
 * CreateTime:2021/11/22 12:00
 * description：
 */
class KeepAliveReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            Intent.ACTION_SCREEN_OFF -> {
                Log.d(OnePieceActivity.TAG, "屏幕关闭，准备拉起OnePieceActivity")
                KeepAliveManager.instance.startOnePieceActivity(context)
            }
            Intent.ACTION_SCREEN_ON -> {
                Log.d(OnePieceActivity.TAG, "屏幕开启，准备关闭OnePieceActivity")
                KeepAliveManager.instance.finishOnePieceActivity()
            }
        }
    }
}