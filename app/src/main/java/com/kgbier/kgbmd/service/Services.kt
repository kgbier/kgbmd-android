package com.kgbier.kgbmd.service

import com.kgbier.kgbmd.BuildConfig
import com.kgbier.kgbmd.addHttpLoggingInterceptorTask
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient

object Services {
    val client: OkHttpClient by lazy {
        OkHttpClient.Builder().apply {
            if (BuildConfig.DEBUG) {
                addHttpLoggingInterceptorTask(this)
            }
        }.build()
    }

    val moshi: Moshi by lazy { Moshi.Builder().add(KotlinJsonAdapterFactory()).build() }
}
