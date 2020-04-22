package com.kgbier.kgbmd.data.imdb.model.jsonld

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * https://schema.org/Movie
 */
@JsonClass(generateAdapter = true)
data class Movie(
    val url: String,
    val name: String,
    val image: String,
    val genre: List<String>,
    val description: String,
    val director: PersonType,
    val creator: List<PersonType>,
    val actor: List<PersonType>,
    val datePublished: String, // Date released
    val aggregateRating: Rating,
    val duration: String
) {
    @JsonClass(generateAdapter = true)
    data class Rating(
        val ratingCount: Int,
        val ratingValue: String
    )

    @JsonClass(generateAdapter = true)
    data class PersonType(
        @Json(name = "@type")
        val type: String,
        val url: String?,
        val name: String?
    )
}