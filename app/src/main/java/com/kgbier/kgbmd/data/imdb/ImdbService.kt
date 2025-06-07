package com.kgbier.kgbmd.data.imdb

import com.kgbier.kgbmd.data.imdb.graphql.GraphqlQuery
import com.kgbier.kgbmd.data.imdb.graphql.MostPopularListQuery
import com.kgbier.kgbmd.data.imdb.graphql.NameDetailsQuery
import com.kgbier.kgbmd.data.imdb.graphql.TitleDetailsQuery
import com.kgbier.kgbmd.data.imdb.model.RatingResponse
import com.kgbier.kgbmd.data.imdb.model.SuggestionResponse
import com.kgbier.kgbmd.data.operation.JsonP
import com.kgbier.kgbmd.service.Services
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.serializer
import okhttp3.HttpUrl
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

object ImdbService {

    private const val graphqEndpoint = "https://api.graphql.imdb.com/"

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

    private suspend fun getHotList(
        type: MostPopularListQuery.Params.ChartTitleType,
    ) = graphqlQuery(
        query = MostPopularListQuery(),
        params = MostPopularListQuery.Params(count = 100, type = type),
    )

    suspend fun getHotMovies() =
        getHotList(MostPopularListQuery.Params.ChartTitleType.MOST_POPULAR_MOVIES)

    suspend fun getHotShows() =
        getHotList(MostPopularListQuery.Params.ChartTitleType.MOST_POPULAR_TV_SHOWS)

    private const val SEARCH_REQUEST_TAG = 451
    suspend fun search(query: String): SuggestionResponse? = withContext(Dispatchers.IO) {
        val validatedQuery = query
            .trim()
            .lowercase()
            .replace(" ", "_")

        if (validatedQuery.isEmpty()) return@withContext null

        val url = buildSuggestionUrl(validatedQuery)

        Services.client.dispatcher.runningCalls().forEach {
            if (it.request().tag() == SEARCH_REQUEST_TAG) it.cancel()
        }

        val request = Request.Builder().tag(SEARCH_REQUEST_TAG).url(url).build()
        val response = Services.client.newCall(request).execute()

        val body = response.body?.string()!!
        json.decodeFromString(body)
    }

    suspend fun getRating(ttid: String): RatingResponse? = withContext(Dispatchers.IO) {
        val url = buildRatingUrl(ttid)

        val request = Request.Builder().url(url).build()
        val response = Services.client.newCall(request).execute()

        val validatedJson = JsonP.toJson(response.body?.string()!!) ?: return@withContext null
        json.decodeFromString(validatedJson)
    }

    suspend fun getTitleDetails(ttid: String) =
        graphqlQuery(TitleDetailsQuery(), TitleDetailsQuery.Params(ttid))

    suspend fun getNameDetails(nmid: String) =
        graphqlQuery(NameDetailsQuery(), NameDetailsQuery.Params(nmid))

    private suspend inline fun <reified Params, reified Result> graphqlQuery(
        query: GraphqlQuery<Params, Result>,
        params: Params?,
    ): Result = graphqlQuery(
        query = query,
        params = params,
        requestSerializer = serializer<GraphqlRequest<Params>>(),
        resultSerializer = serializer<GraphqlResponse<Result>>(),
    )

    val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        explicitNulls = false
    }

    @Serializable
    data class GraphqlResponse<Result>(val data: Result)

    @Serializable
    data class GraphqlRequest<Variables>(val query: String, val variables: Variables?)

    @OptIn(ExperimentalSerializationApi::class)
    private suspend fun <Params, Result> graphqlQuery(
        query: GraphqlQuery<Params, Result>,
        params: Params?,
        requestSerializer: KSerializer<GraphqlRequest<Params>>,
        resultSerializer: KSerializer<GraphqlResponse<Result>>,
    ): Result = withContext(Dispatchers.IO) {
        val graphqlRequest = GraphqlRequest(
            query = query.document
                .replace('\n', ' ')
                .replace("  ", ""),
            variables = params,
        )
        val body = Json.encodeToString(
            serializer = requestSerializer,
            value = graphqlRequest,
        ).toRequestBody()

        val request = Request.Builder()
            .url(graphqEndpoint)
            .header("Content-Type", "application/json")
            .post(body).build()

        val response = Services.client.newCall(request).execute()
        val stream = response.body!!.byteStream()
        json.decodeFromStream(resultSerializer, stream)
            .data
    }
}
