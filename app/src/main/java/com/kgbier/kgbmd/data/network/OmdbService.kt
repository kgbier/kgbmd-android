package com.kgbier.kgbmd.data.network

import com.kgbier.kgbmd.BuildConfig
import com.kgbier.kgbmd.data.Movie
import com.kgbier.kgbmd.service.Services
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl
import okhttp3.Request

object OmdbService {
    private val urlBuilder by lazy {
        HttpUrl.Builder()
            .scheme("http")
            .host("www.omdbapi.com")
            .addQueryParameter("apikey", BuildConfig.API_KEY_OMDB)
    }

    suspend fun getMovieById(ttId: String): Movie = withContext(Dispatchers.IO) {
        val request = Request.Builder()
            .url(urlBuilder.addQueryParameter("i", ttId).build()).build()

        val response = Services.client.newCall(request).execute()
        Services.moshi.adapter(Movie::class.java).fromJson(response.body?.string()!!)!!
    }
}