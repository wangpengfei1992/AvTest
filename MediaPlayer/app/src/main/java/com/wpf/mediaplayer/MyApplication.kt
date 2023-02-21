package com.wpf.mediaplayer

import android.app.Application

/**
 *  Author: feipeng.wang
 *  Time:   2023/2/16
 *  Description : This is description.
 */
class MyApplication : Application() {
    companion object{
        lateinit var application:Application
    }
    override fun onCreate() {
        super.onCreate()
        application = this
    }
}