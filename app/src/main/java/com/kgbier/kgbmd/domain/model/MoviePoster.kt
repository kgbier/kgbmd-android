package com.kgbier.kgbmd.domain.model

import com.kgbier.kgbmd.domain.operation.ImdbImageResizer

data class MoviePoster(
    val ttId: String,
    val title: String,
    val rating: String?,
    val thumbnailUrl: String,
    val posterUrlSmall: String,
    val posterUrlLarge: String
)

fun transformHotListItem(item: HotListItem) = with(item) {
    MoviePoster(
        ttId,
        name,
        rating,
        imageUrl,
        ImdbImageResizer.resize(
            imageUrl,
            ImdbImageResizer.SIZE_WIDTH_MEDIUM
        ),
        ImdbImageResizer.resize(
            imageUrl,
            ImdbImageResizer.SIZE_WIDTH_LARGE
        )
    )
}