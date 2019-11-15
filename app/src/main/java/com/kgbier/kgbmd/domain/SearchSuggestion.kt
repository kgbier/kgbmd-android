package com.kgbier.kgbmd.domain

import com.kgbier.kgbmd.data.imdb.SuggestionResponse

data class SearchSuggestion(
    val ttid: String,
    val title: String,
    val year: String?,
    val tidbit: String?,
    val thumbnailUrl: String?
)

fun transformSuggestionResult(result: SuggestionResponse.Result) = with(result) {
    SearchSuggestion(
        ttid,
        title,
        year,
        tidbit,
        image?.imageUrl
    )
}

fun transformSuggestionResponse(search: SuggestionResponse?) =
    search?.data?.map(::transformSuggestionResult) ?: emptyList()