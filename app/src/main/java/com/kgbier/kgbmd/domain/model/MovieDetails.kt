package com.kgbier.kgbmd.domain.model

import com.kgbier.kgbmd.data.imdb.model.jsonld.Movie

data class MovieDetails(
    val url: String,
    val name: String,
    val thumbnailUrl: String,
    val posterUrlLarge: String,
    val description: String,
    val yearReleased: String,
    val ratingCount: String,
    val ratingValue: String,
    val duration: String
)

fun transformMovieResponse(movie: Movie): MovieDetails? = with(movie) {
    MovieDetails(
        url,
        name,
        image, // TODO
        image, // TODO
        description,
        datePublished, // TODO
        aggregateRating.ratingCount.toString(), // TODO
        aggregateRating.ratingValue,
        duration
    )
}
