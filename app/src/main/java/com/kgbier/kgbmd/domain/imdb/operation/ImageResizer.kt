package com.kgbier.kgbmd.domain.imdb.operation

object ImageResizer {

    private const val CLIPPER_DELIMITER = "._V1"

    /**
     * Thumbnail:
     * [...]_UY67_CR0,0,45,67_AL_.jpg
     *
     * Small:
     * [...]_UX182_CR0,0,182,268_AL_.jpg
     *
     * Large:
     * [...]_SX640_CR0,0,640,999_AL_.jpg
     */

    const val SIZE_FULL = -1
    const val SIZE_WIDTH_THUMBNAIL_CDN = -2
    const val SIZE_WIDTH_HINT = 20
    const val SIZE_WIDTH_THUMBNAIL = 80
    const val SIZE_WIDTH_SMALL = 120
    const val SIZE_WIDTH_MEDIUM = 320
    const val SIZE_WIDTH_LARGE = 640

    fun resize(
        imageUrl: String,
        size: Int = SIZE_WIDTH_SMALL
    ): String {
        val suffix = when (size) {
            SIZE_FULL -> ".jpg"
            SIZE_WIDTH_THUMBNAIL_CDN -> "UY67_CR0,0,45,67_AL_.jpg"
            else -> "_UX$size.jpg"
        }
        return imageUrl.replaceAfter(CLIPPER_DELIMITER, suffix)
    }
}
