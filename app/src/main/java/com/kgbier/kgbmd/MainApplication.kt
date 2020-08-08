package com.kgbier.kgbmd

import android.app.Application
import android.content.Context

class MainApplication : Application() {

    companion object {
        private var mainApplicationContext: Context? = null
        val applicationContext: Context by lazy { mainApplicationContext!! }
    }

    override fun onCreate() {
        super.onCreate()
        mainApplicationContext = applicationContext

        if (BuildConfig.DEBUG) {
            plantTimberTask()
        }

        if (BuildConfig.INTERNAL) {
            developerMenuNotificationTask(this)
        }
    }
}
