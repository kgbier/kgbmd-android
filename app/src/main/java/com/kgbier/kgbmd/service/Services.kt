package com.kgbier.kgbmd.service

import com.kgbier.kgbmd.BuildConfig
import com.kgbier.kgbmd.addHttpLoggingInterceptorTask
import okhttp3.OkHttpClient

object Services {
    val client: OkHttpClient by lazy {
        OkHttpClient.Builder().apply {
            addInterceptor { chain ->
                val baseRequestWithHeaders = chain.request().newBuilder()
                    .addHeader("user-agent", "Android")
                    .build()

                chain.proceed(baseRequestWithHeaders)
            }

            if (BuildConfig.DEBUG) {
                addHttpLoggingInterceptorTask(this)
            }
        }.build()
    }
}
