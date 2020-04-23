package com.kgbier.kgbmd.domain.model

data class Suggestion(
    val id: String,
    val title: String,
    val type: SearchSuggestionType?,
    val year: String?,
    val tidbit: String?,
    val thumbnailUrl: String?
)

enum class SearchSuggestionType {
    MOVIE,      //  q: "feature", "TV movie"
    TV_SHOW,    //  q: "TV mini-series", "TV series"
    CAST_CREW,  // id: "nm0000000"
    GAME        //  q: ""
}

fun getSuggestionType(id: String, type: String?): SearchSuggestionType? = when (type) {
    "feature", "TV movie" -> SearchSuggestionType.MOVIE
    "TV mini-series", "TV series" -> SearchSuggestionType.TV_SHOW
    "video game" -> SearchSuggestionType.GAME
    else -> {
        if (id.startsWith("nm")) SearchSuggestionType.CAST_CREW
        else null
    }
}
