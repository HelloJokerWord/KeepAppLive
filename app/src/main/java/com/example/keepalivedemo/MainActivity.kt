package com.example.keepalivedemo

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.keepalivedemo.oneprocess.OneProcessService
import com.example.keepalivedemo.oneprocess.SharedPreferenceTool


class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "MainActivity"

        /**
         * 判断某个Service是否在运行
         * @param context
         * @param serviceClass 需要查看的Service的Class
         * @return
         */
        fun isServiceRunning(context: Context, serviceClass: Class<OneProcessService>): Boolean {
            val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            activityManager.getRunningServices(Int.MAX_VALUE).forEach { if (serviceClass.name == it.service.className) return true }
            return false
        }
    }

    private var foregroundIntent: Intent? = null

    private var oneProcessService: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.i(TAG, "onCreate")

        //像素保活注册
        //KeepAliveManager.instance.registerKeepLiveReceiver(this)

        //替罪羊服务保活开启
        //foregroundIntent = Intent(this, ForegroundService::class.java)
        //startService(foregroundIntent)

        //单进程广播保活
        if (!isServiceRunning(applicationContext, OneProcessService::class.java)) {
            Log.i(TAG, "检测到服务未在运行,启动服务")
            oneProcessService = Intent(this, OneProcessService::class.java)
            startService(oneProcessService)
        } else {
            Log.i(TAG, "检测到服务正在运行,无需再次启动")
        }

    }

    override fun onStart() {
        Log.i(TAG, "onStart")
        super.onStart()
    }

    override fun onRestart() {
        Log.i(TAG, "onRestart")
        super.onRestart()
    }

    override fun onResume() {
        Log.i(TAG, "onResume")
        super.onResume()
    }

    override fun onPause() {
        Log.i(TAG, "onPause")
        super.onPause()
    }

    override fun onStop() {
        Log.i(TAG, "onStop")
        super.onStop()
    }

    override fun onDestroy() {
        Log.i(TAG, "onDestroy")
        //像素保活注销
        //KeepAliveManager.instance.unregisterKeepLiveReceiver(this)

        //替罪羊服务保活关闭
        //foregroundIntent?.let { stopService(it) }

        //单进程广播保活关闭
        oneProcessService?.let { stopService(it) }
        SharedPreferenceTool.instance.putInt(OneProcessService.KEY_COUNT, OneProcessService.count)

        super.onDestroy()
    }
}