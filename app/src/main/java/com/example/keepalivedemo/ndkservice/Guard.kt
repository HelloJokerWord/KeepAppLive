package com.example.keepalivedemo.ndkservice

/**
 * CreateBy:Joker
 * CreateTime:2021/11/30 10:22
 * description：
 */
class Guard {
    companion object {
        init {
            System.loadLibrary("native-lib")
        }
    }

    external fun create(userId: String?)

    external fun connect()
}