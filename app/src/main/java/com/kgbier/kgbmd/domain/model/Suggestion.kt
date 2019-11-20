package com.kgbier.kgbmd.domain.model

import com.kgbier.kgbmd.data.imdb.model.SuggestionResponse
import com.kgbier.kgbmd.domain.operation.ImdbImageResizer

data class Suggestion(
    val id: String,
    val title: String,
    val type: SearchSuggestionType?,
    val year: String?,
    val tidbit: String?,
    val thumbnailUrl: String?
)

fun transformSuggestionResult(result: SuggestionResponse.Result) = with(result) {
    Suggestion(
        id,
        title,
        mapSuggestionType(id, type),
        year,
        tidbit,
        image?.imageUrl?.let { ImdbImageResizer.resize(it, ImdbImageResizer.SIZE_WIDTH_THUMBNAIL) }
    )
}

fun transformSuggestionResponse(search: SuggestionResponse?) =
    search?.data?.map(::transformSuggestionResult) ?: emptyList()

enum class SearchSuggestionType {
    MOVIE,      //  q: "feature", "TV movie"
    TV_SHOW,    //  q: "TV mini-series", "TV series"
    CAST_CREW,  // id: "nm0000000"
    GAME        //  q: ""
}

fun mapSuggestionType(id: String, type: String?): SearchSuggestionType? = when (type) {
    "feature", "TV movie" -> SearchSuggestionType.MOVIE
    "TV mini-series", "TV series" -> SearchSuggestionType.TV_SHOW
    "video game" -> SearchSuggestionType.GAME
    else -> {
        if (id.startsWith("nm")) SearchSuggestionType.CAST_CREW
        else null
    }
}
