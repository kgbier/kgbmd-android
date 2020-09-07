package com.kgbier.kgbmd.data.imdb.model

import com.kgbier.kgbmd.domain.imdb.operation.ImageResizer
import com.kgbier.kgbmd.domain.model.MoviePoster

data class HotListItem(
    val ttId: String,
    val name: String,
    val rating: String?,
    val imageUrl: String
)

fun transformHotListItem(item: HotListItem) = with(item) {
    MoviePoster(
        ttId,
        name,
        rating.takeUnless { it == "0.0" },
        imageUrl, // Keep the same URL as the public site to try and take advantage of any CDN caching
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
