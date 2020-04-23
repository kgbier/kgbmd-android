package com.kgbier.kgbmd

import android.app.Application
import android.content.Context
import timber.log.Timber

class MainApplication : Application() {
    companion object {
        private var mainApplicationContext: Context? = null
        val applicationContext: Context by lazy { mainApplicationContext!! }
    }

    override fun onCreate() {
        super.onCreate()
        mainApplicationContext = this.applicationContext

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}