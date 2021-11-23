package com.example.keepalivedemo.onepiece

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity

class OnePieceActivity : AppCompatActivity() {

    companion object {
        const val TAG = "OnePieceKeepLive"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate")

        //左上角显示
        window.setGravity(Gravity.START or Gravity.TOP)

        //设置为1像素大小
        val params = window.attributes
        params.apply {
            x = 0
            y = 0
            width = 1
            height = 1
        }
        window.attributes = params

        //KeepAliveManager中的保活Activity初始化为本Activity
        KeepAliveManager.instance.setKeepLiveActivity(this)
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
        super.onDestroy()
    }
}