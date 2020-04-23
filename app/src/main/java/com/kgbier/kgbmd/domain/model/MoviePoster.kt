package com.kgbier.kgbmd.domain.model

import com.kgbier.kgbmd.domain.imdb.operation.ImageResizer

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
        ImageResizer.resize(
            imageUrl,
            ImageResizer.SIZE_WIDTH_MEDIUM
        ),
        ImageResizer.resize(
            imageUrl,
            ImageResizer.SIZE_WIDTH_LARGE
        )
    )
}
