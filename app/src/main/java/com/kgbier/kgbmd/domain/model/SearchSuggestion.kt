package com.kgbier.kgbmd.domain.model

import com.kgbier.kgbmd.data.imdb.model.SuggestionResponse
import com.kgbier.kgbmd.domain.operation.ImdbImageResizer

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
        image?.imageUrl?.let { ImdbImageResizer.resize(it, ImdbImageResizer.SIZE_WIDTH_THUMBNAIL) }
    )
}

fun transformSuggestionResponse(search: SuggestionResponse?) =
    search?.data?.map(::transformSuggestionResult) ?: emptyList()