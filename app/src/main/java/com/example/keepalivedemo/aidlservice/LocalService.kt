package com.example.keepalivedemo.aidlservice

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import com.example.keepalivedemo.IKeepAliveAidlInterface
import com.example.keepalivedemo.MainActivity
import java.util.*

/**
 * CreateBy:Joker
 * CreateTime:2021/11/29 17:30
 * description：
 */
class LocalService : Service() {

    companion object {
        const val TAG = "aidlService"
        var count = 0
    }

    private var timer: Timer? = null
    private var timerTask: TimerTask? = null

    private var binder: IBinder? = null
    private var serviceConnection: ServiceConnection? = null

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "LocalService onCreate")
        binder = LocalServiceBinder()
        serviceConnection = object : ServiceConnection {

            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                Log.i(TAG, "触发LocalService onServiceConnected")
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                Log.e(TAG, "触发LocalService onServiceDisconnected")
                wakeService()
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(TAG, "LocalService onStartCommand")
        startTask()
        wakeService()
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.i(TAG, "LocalService onBind")
        return binder
    }

    override fun onDestroy() {
        stopTask()
        stopSelf()
        serviceConnection?.let { unbindService(it) }
        Log.e(TAG, "LocalService onDestroy")
        super.onDestroy()
    }

    /**
     * 开启定时任务，count每秒+1
     */
    private fun startTask() {
        timer = Timer()
        timerTask = object : TimerTask() {
            override fun run() {
                Log.i(TAG, "LocalService 服务运行中，count: $count")
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

    private fun wakeService() {
        if (!MainActivity.isServiceRunning(this, RemoteService::class.java)) {
            startService(Intent(this, RemoteService::class.java))
        }
        serviceConnection?.let { bindService(Intent(this, RemoteService::class.java), it, Context.BIND_IMPORTANT) }
    }

    class LocalServiceBinder : IKeepAliveAidlInterface.Stub()
}