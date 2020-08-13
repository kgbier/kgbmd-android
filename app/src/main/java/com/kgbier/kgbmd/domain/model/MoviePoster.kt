package com.kgbier.kgbmd.domain.model

data class MoviePoster(
    val ttId: String,
    val title: String,
    val rating: String?,
    val thumbnailUrl: String,
    val posterUrlSmall: String,
    val posterUrlLarge: String
)
