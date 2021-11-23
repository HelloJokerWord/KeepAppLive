package com.example.keepalivedemo.onepiece

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import java.lang.ref.WeakReference

/**
 * CreateBy:Joker
 * CreateTime:2021/11/22 11:59
 * description：
 */
class KeepAliveManager {

    companion object {
        val instance: KeepAliveManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { KeepAliveManager() }
    }

    private var activity: WeakReference<OnePieceActivity?>? = null  //弱引用，防止内存泄漏
    private var receiver: KeepAliveReceiver? = null

    fun setKeepLiveActivity(activity: OnePieceActivity?) {
        this.activity = WeakReference(activity)
    }

    /**
     * 开启保活Activity
     */
    fun startOnePieceActivity(context: Context?) {
        val intent = Intent(context, OnePieceActivity::class.java)
        Log.i(OnePieceActivity.TAG, "${context == null}")
        context?.startActivity(intent)
    }

    /**
     * 关闭保活Activity
     */
    fun finishOnePieceActivity() {
        activity?.get()?.finish()
    }

    /**
     * 注册广播
     */
    fun registerKeepLiveReceiver(context: Context?) {
        Log.i(OnePieceActivity.TAG, "KeepAliveReceiver已注册")
        receiver = KeepAliveReceiver()
        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_SCREEN_OFF)
        filter.addAction(Intent.ACTION_SCREEN_ON)
        context?.registerReceiver(receiver, filter)
    }

    /**
     * 注销广播
     */
    fun unregisterKeepLiveReceiver(context: Context?) {
        Log.i(OnePieceActivity.TAG, "KeepAliveReceiver已注销")
        receiver?.let {
            context?.unregisterReceiver(it)
        }
    }


}