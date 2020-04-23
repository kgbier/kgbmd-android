package com.kgbier.kgbmd.data.omdb

import com.kgbier.kgbmd.BuildConfig
import com.kgbier.kgbmd.data.omdb.model.Movie
import com.kgbier.kgbmd.service.Services
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl
import okhttp3.Request

object OmdbService {
    private fun buildMovieByIdUrl(ttid: String) = HttpUrl.Builder()
        .scheme("http")
        .host("www.omdbapi.com")
        .addQueryParameter("apikey", BuildConfig.API_KEY_OMDB)
        .addQueryParameter("i", ttid)
        .build()

    suspend fun getMovieById(ttid: String): Movie? = withContext(Dispatchers.IO) {
        val url = buildMovieByIdUrl(ttid)

        val request = Request.Builder().url(url).build()

        val response = Services.client.newCall(request).execute()
        Services.moshi.adapter(Movie::class.java).fromJson(response.body?.string()!!)
    }
}
