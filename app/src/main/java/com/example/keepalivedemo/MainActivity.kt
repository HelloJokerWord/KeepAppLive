package com.example.keepalivedemo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.keepalivedemo.onepiece.KeepAliveManager

class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.i(TAG,"onCreate")

        KeepAliveManager.instance.registerKeepLiveReceiver(this)
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
        KeepAliveManager.instance.unregisterKeepLiveReceiver(this)
        super.onDestroy()
    }

}