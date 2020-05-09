package com.kgbier.kgbmd.domain.model

data class TitleDetails(
    val name: String,
    val poster: Poster?,
    val contentRating: String,
    val genre: String,
    val directedBy: String?,
    val writtenBy: String?,
    val createdBy: String?,
    val description: String?,
    val yearReleased: String?,
    val rating: Rating?,
    val duration: String?
) {
    data class Poster(val thumbnailUrl: String, val largeUrl: String)
    data class Rating(val value: String, val best: String, val count: String?)
}
