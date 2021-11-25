package com.example.keepalivedemo.service

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.keepalivedemo.MainActivity
import com.example.keepalivedemo.R
import java.util.*


/**
 * CreateBy:Joker
 * CreateTime:2021/11/25 10:31
 * description：
 */
class ForegroundService : Service() {

    companion object {
        const val SERVICE_ID = 1
        const val CHANNEL_ID = "1"
        const val TAG = "KeepLiveService"
        var count = 0
    }

    private var timer: Timer? = null
    private var timerTask: TimerTask? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "onCreate 创建前台服务")
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startTask()

        //判断版本
        when {
            //Android4.3以下版本
            //Build.VERSION.SDK_INT < 18 -> {
            //    //直接调用startForeground即可,不会在通知栏创建通知
            //    startForeground(SERVICE_ID, Notification())
            //}
            Build.VERSION.SDK_INT < 24 -> { //Android4.3 - 7.0之间
                val scapegoatIntent = Intent(this, ScapegoatService::class.java)
                startService(scapegoatIntent)
            }
            //Android 8.0以上
            else -> {

            }
        }
        return START_STICKY
    }

    override fun onDestroy() {
        stopTask()
        Log.i(TAG, "onDestroy 销毁前台服务")
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
                if (count % 10 == 0) {
                    showNotification()
                }
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

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun showNotification() {
        try {
            Log.w(TAG, "showNotification")
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //当Android版本大于等于8时，创建通知渠道（Notification Channels）
                val channel = NotificationChannel(CHANNEL_ID, "channel1", NotificationManager.IMPORTANCE_HIGH)
                channel.apply {
                    setSound(null, null)
                    lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                    enableLights(true)  //是否在桌面icon右上角展示小红点
                    lightColor = Color.BLUE   //小红点颜色
                    setShowBadge(true)        //是否在久按桌面图标时显示此渠道的通知
                }
                manager?.createNotificationChannel(channel)
            }

            val intentNotification = Intent(this, MainActivity::class.java)
            intentNotification.let {
                it.putExtra("title", "title")
                it.putExtra("content", "content")
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                it.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                it.action = Intent.ACTION_VIEW
            }
            val pendingIntent = PendingIntent.getActivity(this, 10086, intentNotification, PendingIntent.FLAG_UPDATE_CURRENT)

            val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            //自定义
            //val remoteViews = RemoteViews(packageName, R.layout.remote_push_noti_layout)
            //builder.setContent(remoteViews)

            //系统样式
            val notification = builder
                .setContentTitle("title")              //通知标题
                .setContentText("content")             //通知内容
                .setWhen(System.currentTimeMillis())   //指定通知被创建的时间
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher)    //使用小图标
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
                .build()

            manager?.notify(System.currentTimeMillis().toInt(), notification)

            //经测试，本人的一加6T（Android 10）这样写并不会在通知栏创建通知，其他机型与版本效果仍需考证
            //startForeground(SERVICE_ID, notification)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}