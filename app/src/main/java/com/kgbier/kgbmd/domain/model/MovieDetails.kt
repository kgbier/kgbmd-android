package com.kgbier.kgbmd.domain.model

data class MovieDetails(
    val name: String,
    val thumbnailUrl: String,
    val posterUrlLarge: String,
    val contentRating: String,
    val genre: String,
    val directedBy: String,
    val writtenBy: String,
    val description: String,
    val yearReleased: String,
    val rating: Rating?,
    val duration: String?
) {
    data class Rating(val count: String, val value: String)
}
