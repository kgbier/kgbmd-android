package com.kgbier.kgbmd.data.imdb

import com.kgbier.kgbmd.data.imdb.model.HotListItem
import com.kgbier.kgbmd.data.imdb.model.RatingResponse
import com.kgbier.kgbmd.data.imdb.model.SuggestionResponse
import com.kgbier.kgbmd.data.imdb.model.TitleInfo
import com.kgbier.kgbmd.data.imdb.operation.HotListParser
import com.kgbier.kgbmd.data.imdb.operation.MediaEntityInfoParser
import com.kgbier.kgbmd.data.operation.JsonP
import com.kgbier.kgbmd.service.Services
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl
import okhttp3.Request
import java.util.*

object ImdbService {

    // https://v2.sg.media-imdb.com/suggestion/d/dark_knight.json
    private fun buildSuggestionUrl(query: String) = HttpUrl.Builder()
        .scheme("https")
        .host("v2.sg.media-imdb.com")
        .addPathSegment("suggestion")
        .addPathSegment(query.first().toString())
        .addPathSegment("$query.json")
        .build()

    // https://p.media-imdb.com/static-content/documents/v1/title/tt0468569/ratings%3Fjsonp=imdb.rating.run:imdb.api.title.ratings/data.json
    private fun buildRatingUrl(ttid: String) = HttpUrl.Builder()
        .scheme("https")
        .host("p.media-imdb.com")
        .addPathSegments("static-content/documents/v1/title")
        .addPathSegment(ttid)
        .addEncodedPathSegment("ratings%3Fjsonp=imdb.rating.run:imdb.api.title.ratings/data.json")
        .build()

    // https://www.imdb.com/title/tt2527336/
    private fun buildTitleDetailsUrl(ttid: String) = HttpUrl.Builder()
        .scheme("https")
        .host("www.imdb.com")
        .addPathSegments("title")
        .addPathSegment(ttid)
        .build()

    private suspend fun getHotList(listUrl: String) = withContext(Dispatchers.IO) {
        val request = Request.Builder()
            .url(listUrl).build()

        val response = Services.client.newCall(request).execute()
        HotListParser(response.body?.source()!!).getList()
    }

    private const val METER_MOVIE = "https://www.imdb.com/chart/moviemeter"
    suspend fun getHotMovies(): List<HotListItem> = getHotList(METER_MOVIE)

    private const val METER_TV = "https://www.imdb.com/chart/tvmeter"
    suspend fun getHotShows(): List<HotListItem> = getHotList(METER_TV)

    private const val SEARCH_REQUEST_TAG = 451
    suspend fun search(query: String): SuggestionResponse? = withContext(Dispatchers.IO) {
        val validatedQuery = query
            .trim()
            .toLowerCase(Locale.ROOT)
            .replace(" ", "_")

        if (validatedQuery.isEmpty()) return@withContext null

        val url = buildSuggestionUrl(validatedQuery)

        Services.client.dispatcher.runningCalls().forEach {
            if (it.request().tag() == SEARCH_REQUEST_TAG) it.cancel()
        }

        val request = Request.Builder().tag(SEARCH_REQUEST_TAG).url(url).build()
        val response = Services.client.newCall(request).execute()

        val body = response.body?.string()!!
        Services.moshi.adapter(SuggestionResponse::class.java).fromJson(body)!!
    }

    suspend fun getRating(ttid: String): RatingResponse? = withContext(Dispatchers.IO) {
        val url = buildRatingUrl(ttid)

        val request = Request.Builder().url(url).build()
        val response = Services.client.newCall(request).execute()

        val validatedJson = JsonP.toJson(response.body?.string()!!) ?: return@withContext null
        Services.moshi.adapter(RatingResponse::class.java).fromJson(validatedJson)!!
    }

    suspend fun getTitleDetails(ttid: String): TitleInfo? = withContext(Dispatchers.IO) {
        val url = buildTitleDetailsUrl(ttid)
        val request = Request.Builder().url(url).build()

        val response = Services.client.newCall(request).execute()
        MediaEntityInfoParser(response.body?.source()!!).getTitleInfo()
    }
}
