package com.kgbier.kgbmd.data.imdb.model

import com.kgbier.kgbmd.domain.imdb.operation.ImageResizer
import com.kgbier.kgbmd.domain.model.Suggestion
import com.kgbier.kgbmd.domain.model.getSuggestionType
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SuggestionResponse(
    @Json(name = "d")
    val data: List<Result>?,
    @Json(name = "q")
    val query: String,
    @Json(name = "v")
    val version: Int
) {
    @JsonClass(generateAdapter = true)
    data class Result(
        @Json(name = "id")
        val id: String,
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
        @JsonClass(generateAdapter = true)
        data class Image(
            val width: Int?,
            val height: Int?,
            val imageUrl: String
        )
    }
}

fun transformSuggestionResult(result: SuggestionResponse.Result) = with(result) {
    Suggestion(
        id,
        title,
        getSuggestionType(id, type),
        year,
        tidbit,
        image?.imageUrl?.let {
            ImageResizer.resize(
                it,
                ImageResizer.SIZE_WIDTH_THUMBNAIL
            )
        }
    )
}

fun transformSuggestionResponse(search: SuggestionResponse?) =
    search?.data?.map(::transformSuggestionResult) ?: emptyList()
