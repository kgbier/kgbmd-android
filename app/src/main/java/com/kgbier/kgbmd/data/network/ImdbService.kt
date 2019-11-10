package com.kgbier.kgbmd.data.network

import com.kgbier.kgbmd.data.parse.HotListItem
import com.kgbier.kgbmd.data.parse.HotListParser
import com.kgbier.kgbmd.service.Services
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Request

object ImdbService {
    private const val METER_MOVIE = "https://www.imdb.com/chart/moviemeter"
    private const val METER_TV = "https://www.imdb.com/chart/tvmeter"

    suspend fun getHotMovies(): List<HotListItem> = withContext(Dispatchers.IO) {
        val request = Request.Builder()
            .url(METER_MOVIE).build()

        val response = Services.client.newCall(request).execute()
        HotListParser(response.body?.source()!!).getListItems()
    }
}

