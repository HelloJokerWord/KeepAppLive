package com.example.keepalivedemo.oneprocess

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import java.util.*

/**
 * CreateBy:Joker
 * CreateTime:2021/11/29 16:25
 * description：
 */
class OneProcessService : Service() {

    companion object {
        const val TAG = "OneProcess"
        const val KEY_COUNT = "KEY_COUNT"
        var count = 0
    }

    private var timer: Timer? = null
    private var timerTask: TimerTask? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "服务创建了")
        val saveCount = SharedPreferenceTool.instance.getInt(KEY_COUNT, -1)
        if (saveCount == -1) {
            count = 0
            Log.i(TAG, "count首次启动，从0开始计数")
        } else {
            count = saveCount
            Log.i(TAG, "count从上次保存的 $saveCount 开始计数")
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startTask()
        return START_STICKY
    }

    override fun onDestroy() {
        stopTask()
        Log.i(TAG, "服务停止了")

        val oneProgressIntent = Intent(this, OneProcessReceiver::class.java)
        sendBroadcast(oneProgressIntent)
        Log.w(TAG, "发送保活广播")
        super.onDestroy()
    }

    /**
     * 开启定时任务，count每秒+1
     */
    private fun startTask() {
        timer = Timer()
        timerTask = object : TimerTask() {
            override fun run() {
                Log.i(TAG, "服务运行中，count: $count")
                count++
            }
        }
        timer?.schedule(timerTask, 0, 1000)
    }

    /**
     * 结束定时任务
     */
    private fun stopTask() {
        timer?.cancel()
        timer = null
        timerTask?.cancel()
        timerTask = null
        count = 0
    }
}