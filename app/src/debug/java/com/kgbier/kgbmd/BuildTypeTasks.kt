package com.kgbier.kgbmd

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber

fun plantTimberTask() {
    Timber.plant(Timber.DebugTree())
}

fun addHttpLoggingInterceptorTask(builder: OkHttpClient.Builder) {
    HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }.also { builder.addInterceptor(it) }
}
