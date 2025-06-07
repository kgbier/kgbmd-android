package com.kgbier.kgbmd.data.imdb.model

import com.kgbier.kgbmd.domain.imdb.operation.ImageResizer
import com.kgbier.kgbmd.domain.model.Suggestion
import com.kgbier.kgbmd.domain.model.getSuggestionType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SuggestionResponse(
    @SerialName("d")
    val data: List<Result>?,
    @SerialName("q")
    val query: String,
    @SerialName("v")
    val version: Int,
) {
    @Serializable
    data class Result(
        @SerialName("id")
        val id: String,
        @SerialName("l")
        val title: String,
        @SerialName("i")
        val image: Image?,
        @SerialName("q")
        val type: String?,
        val rank: Int?,
        @SerialName("s")
        val tidbit: String?,
        @SerialName("y")
        val year: String?,
    ) {
        @Serializable
        data class Image(
            val width: Int?,
            val height: Int?,
            val imageUrl: String,
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
                imageUrl = it,
                size = ImageResizer.SIZE_WIDTH_THUMBNAIL,
            )
        }
    )
}

fun transformSuggestionResponse(search: SuggestionResponse?) =
    search?.data?.map(::transformSuggestionResult) ?: emptyList()
