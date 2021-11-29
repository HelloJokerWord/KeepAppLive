package com.example.keepalivedemo.oneprocess

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import com.example.keepalivedemo.MainActivity

/**
 * CreateBy:Joker
 * CreateTime:2021/11/29 16:44
 * description：
 */


class SharedPreferenceTool {

    companion object {
        val instance by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { SharedPreferenceTool() }
    }

    private var sharedPreferences: SharedPreferences? = null
    private var editor: Editor? = null

    fun init(context: Context){
        sharedPreferences = context.getSharedPreferences("preferences", Context.MODE_PRIVATE)
        editor = sharedPreferences?.edit()
    }

    /**
     * 往SharedPreference存放整型数据
     */
    fun putInt(key: String?, value: Int) {
        editor?.putInt(key, value)
        editor?.commit()
    }

    /**
     * 从SharedPreference取出整型数据
     */
    fun getInt(key: String?, defaultValue: Int): Int {
        return sharedPreferences?.getInt(key, defaultValue) ?: 0
    }

}
