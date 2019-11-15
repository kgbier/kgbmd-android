package com.kgbier.kgbmd.data.imdb

import com.squareup.moshi.Json

data class SuggestionResponse(
    @Json(name = "d")
    val data: List<Result>?,
    @Json(name = "q")
    val query: String,
    @Json(name = "v")
    val version: Int
) {
    data class Result(
        @Json(name = "id")
        val ttid: String,
        @Json(name = "l")
        val title: String,
        @Json(name = "i")
        val image: Image?,
        @Json(name = "q")
        val type: String?,
        val rank: Int?,
        @Json(name = "s")
        val tidbit: String?,
        @Json(name = "y")
        val year: String?
    ) {
        data class Image(
            val width: Int?,
            val height: Int?,
            val imageUrl: String
        )
    }
}