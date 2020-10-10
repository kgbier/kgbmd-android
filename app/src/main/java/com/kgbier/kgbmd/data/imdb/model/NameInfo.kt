package com.kgbier.kgbmd.data.imdb.model

data class NameInfo(
    val name: String,
    val headshotUrl: String?,
    val description: String?,
    val filmography: List<Film>,
) {
    data class Film(val name: String, val role: String)
}
