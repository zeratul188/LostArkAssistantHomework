package com.lostark.lostarkassistanthomework

import android.app.Application
import android.content.Context

class App : Application() {
    init {
        instance = this
    }

    companion object {
        var instance :App? = null
        lateinit var prefs: PreferenceUtil
        fun context(): Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        prefs = PreferenceUtil(applicationContext)
        super.onCreate()
    }
}