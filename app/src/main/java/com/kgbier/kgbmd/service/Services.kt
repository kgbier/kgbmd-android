package com.kgbier.kgbmd.service

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient

object Services {
    val client: OkHttpClient by lazy { OkHttpClient() }
    val moshi by lazy { Moshi.Builder().add(KotlinJsonAdapterFactory()).build() }
}