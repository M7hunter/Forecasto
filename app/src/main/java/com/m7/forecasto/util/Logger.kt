package com.m7.forecasto.util

import android.util.Log
import com.m7.forecasto.BuildConfig

object Logger {

    fun d(tag: String = "general", text: String) {
        if (BuildConfig.DEBUG) {
            Log.d("check_$tag", text)
        }
    }
}