package com.kgbier.kgbmd.domain

import com.kgbier.kgbmd.data.imdb.SuggestionResponse

data class SearchSuggestion(
    val title: String,
    val year: String?,
    val tidbit: String?
)

fun transformSuggestionResult(result: SuggestionResponse.Result) = with(result) {
    SearchSuggestion(
        title,
        year,
        tidbit
    )
}

fun transformSuggestionResponse(search: SuggestionResponse?) =
    search?.data?.map(::transformSuggestionResult) ?: emptyList()