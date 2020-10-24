package com.kgbier.kgbmd.domain.model

sealed class MediaEntityDetails

data class NameDetails(
    val name: String,
    val headshot: Image?,
    val description: String?,
    val filmography: Map<FilmographicCategory, List<Title>>,
) : MediaEntityDetails() {
    data class Title(val name: String?, val year: String?, val role: String?)
}

data class TitleDetails(
    val name: String,
    val poster: Image?,
    val contentRating: String,
    val genre: String,
    val directedBy: String?,
    val writtenBy: String?,
    val createdBy: String?,
    val description: String?,
    val yearReleased: String?,
    val rating: Rating?,
    val duration: String?,
    val castMembers: List<CastMember>,
) : MediaEntityDetails() {
    data class Rating(val value: String, val best: String, val count: String?)
    data class CastMember(val thumbnailUrl: String?, val name: String?, val role: String?)
}
