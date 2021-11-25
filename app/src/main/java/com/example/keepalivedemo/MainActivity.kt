package com.example.keepalivedemo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.keepalivedemo.service.ForegroundService


class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "MainActivity"
    }

    private var foregroundIntent: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.i(TAG,"onCreate")

        //像素保活注册
        //KeepAliveManager.instance.registerKeepLiveReceiver(this)

        //替罪羊服务保活开启
        foregroundIntent = Intent(this, ForegroundService::class.java)
        startService(foregroundIntent)
    }

    override fun onStart() {
        Log.i(TAG,"onStart")
        super.onStart()
    }

    override fun onRestart() {
        Log.i(TAG,"onRestart")
        super.onRestart()
    }

    override fun onResume() {
        Log.i(TAG,"onResume")
        super.onResume()
    }

    override fun onPause() {
        Log.i(TAG,"onPause")
        super.onPause()
    }

    override fun onStop() {
        Log.i(TAG,"onStop")
        super.onStop()
    }

    override fun onDestroy() {
        Log.i(TAG,"onDestroy")
        //像素保活注销
        //KeepAliveManager.instance.unregisterKeepLiveReceiver(this)

        //替罪羊服务保活关闭
        stopService(foregroundIntent)

        super.onDestroy()
    }

}