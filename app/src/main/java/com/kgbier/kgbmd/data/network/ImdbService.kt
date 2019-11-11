package com.kgbier.kgbmd.data.network

import com.kgbier.kgbmd.data.imdb.RatingResponse
import com.kgbier.kgbmd.data.imdb.SuggestionResponse
import com.kgbier.kgbmd.data.parse.HotListItem
import com.kgbier.kgbmd.data.parse.HotListParser
import com.kgbier.kgbmd.data.parse.JsonP
import com.kgbier.kgbmd.service.Services
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl
import okhttp3.Request
import java.util.*

object ImdbService {
    private const val METER_MOVIE = "https://www.imdb.com/chart/moviemeter"
    private const val METER_TV = "https://www.imdb.com/chart/tvmeter"

    // https://v2.sg.media-imdb.com/suggestion/d/dark_knight.json
    private val SuggestionUrlBuilder: HttpUrl.Builder by lazy {
        HttpUrl.Builder()
            .scheme("https")
            .host("v2.sg.media-imdb.com")
            .addPathSegment("suggestion")
    }

    // https://p.media-imdb.com/static-content/documents/v1/title/tt0468569/ratings%3Fjsonp=imdb.rating.run:imdb.api.title.ratings/data.json
    private val RatingUrlBuilder: HttpUrl.Builder by lazy {
        HttpUrl.Builder()
            .scheme("https")
            .host("p.media-imdb.com")
            .addPathSegments("static-content/documents/v1/title")
    }

    private fun HttpUrl.Builder.buildRatingUrl() =
        addEncodedPathSegment("ratings%3Fjsonp=imdb.rating.run:imdb.api.title.ratings/data.json")
            .build()

    suspend fun getHotMovies(): List<HotListItem> = withContext(Dispatchers.IO) {
        val request = Request.Builder()
            .url(METER_MOVIE).build()

        val response = Services.client.newCall(request).execute()
        HotListParser(response.body?.source()!!).getListItems()
    }

    suspend fun search(query: String): List<SuggestionResponse.Result> =
        withContext(Dispatchers.IO) {
            val validatedQuery = query
                .trim()
                .toLowerCase(Locale.ROOT)
                .replace(" ", "_")

            if (validatedQuery.isEmpty()) return@withContext emptyList<SuggestionResponse.Result>()

            val url = SuggestionUrlBuilder
                .addPathSegment(validatedQuery.first().toString())
                .addPathSegment("$validatedQuery.json").build()

            val request = Request.Builder().url(url).build()
            val response = Services.client.newCall(request).execute()

            Services.moshi.adapter(SuggestionResponse::class.java).fromJson(response.body?.string()!!)!!.data
        }

    suspend fun getRating(ttid: String): RatingResponse.RatingInfo = withContext(Dispatchers.IO) {
        val url = RatingUrlBuilder
            .addPathSegment(ttid)
            .buildRatingUrl()

        val request = Request.Builder().url(url).build()
        val response = Services.client.newCall(request).execute()

        val validatedJson = JsonP.toJson(response.body?.string()!!)
        Services.moshi.adapter(RatingResponse::class.java).fromJson(validatedJson)!!.ratingInfo
    }
}

