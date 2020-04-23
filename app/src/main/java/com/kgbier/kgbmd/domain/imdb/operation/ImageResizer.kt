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

    const val SIZE_WIDTH_THUMBNAIL = 80
    const val SIZE_WIDTH_SMALL = 120
    const val SIZE_WIDTH_MEDIUM = 320
    const val SIZE_WIDTH_LARGE = 640

    fun resize(
        imageUrl: String,
        size: Int = SIZE_WIDTH_SMALL
    ): String {
        return imageUrl.replaceAfter(CLIPPER_DELIMITER, "_UX$size.jpg")
    }
}
