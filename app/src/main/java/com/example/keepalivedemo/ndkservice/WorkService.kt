package com.example.keepalivedemo.ndkservice

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Process
import android.util.Log
import com.example.keepalivedemo.oneprocess.OneProcessService
import java.util.*

/**
 * CreateBy:Joker
 * CreateTime:2021/11/30 10:28
 * description：
 */
class WorkService : Service() {

    companion object {
        const val TAG = "WorkService"
        var count = 0
    }

    private var timer: Timer? = null
    private var timerTask: TimerTask? = null

    private var guard: Guard? = null

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "onCreate")
        guard = Guard()
        guard?.create(Process.myPid().toString())
        guard?.connect()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(TAG, "onStartCommand")
        startTask()
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        Log.w(TAG, "onDestroy")
        stopTask()
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