package com.kgbier.kgbmd.domain.model

import com.kgbier.kgbmd.domain.operation.HotListItem

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
        generatePosterUrl(
            imageUrl,
            DYNAMIC_IMAGE_SMALL
        ),
        generatePosterUrl(
            imageUrl,
            DYNAMIC_IMAGE_LARGE
        )
    )
}

const val DYNAMIC_IMAGE_SIZE_DELIMITER = "._V1"
const val DYNAMIC_IMAGE_THUMBNAIL = "_UY67_CR0,0,45,67_AL_.jpg"
const val DYNAMIC_IMAGE_SMALL = "_UX182_CR0,0,182,268_AL_.jpg"
const val DYNAMIC_IMAGE_LARGE = "_SX640_CR0,0,640,999_AL_.jpg"

private fun generatePosterUrl(
    thumbnailUrl: String,
    size: String = DYNAMIC_IMAGE_SMALL
): String {
    return thumbnailUrl.replaceAfter(DYNAMIC_IMAGE_SIZE_DELIMITER, size)
}
