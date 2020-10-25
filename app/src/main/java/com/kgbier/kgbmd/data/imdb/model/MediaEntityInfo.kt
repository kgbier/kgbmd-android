package com.kgbier.kgbmd.data.imdb.model

import com.kgbier.kgbmd.domain.imdb.operation.ImageResizer
import com.kgbier.kgbmd.domain.model.FilmographicCategory
import com.kgbier.kgbmd.domain.model.NameDetails
import com.kgbier.kgbmd.domain.model.TitleDetails

sealed class MediaEntityInfo

data class NameInfo(
    val name: String,
    val headshotUrl: String?,
    val description: String?,
    val filmography: List<Title>,
) : MediaEntityInfo() {
    data class Title(
        val category: String,
        val titleId: String,
        val name: String,
        val year: String?,
        val role: String?,
    )
}

fun transformNameInfo(nameInfo: NameInfo): NameDetails? = with(nameInfo) {
    NameDetails(
        name,
        headshotUrl?.let(::transformImageUrl),
        description,
        transformFilmography(filmography),
    )
}

fun transformFilmography(filmography: List<NameInfo.Title>): Map<FilmographicCategory, List<NameDetails.Title>> {
    val categoryMap = mutableMapOf<FilmographicCategory, MutableList<NameDetails.Title>>()
    fun insertTitle(category: FilmographicCategory, title: NameDetails.Title) {
        val list = categoryMap.getOrPut(category) { mutableListOf() }
        list.add(title)
    }

    filmography.forEach {
        val title = NameDetails.Title(
            it.name,
            it.year?.ifBlank { null },
            it.role?.ifBlank { null },
        )
        val category = when (it.category) {
            "actor" -> FilmographicCategory.ACTOR
            "actress" -> FilmographicCategory.ACTRESS
            "director" -> FilmographicCategory.DIRECTOR
            "composer" -> FilmographicCategory.COMPOSER
            "self" -> FilmographicCategory.SELF
            "archive_footage" -> FilmographicCategory.ARCHIVE_FOOTAGE
            "soundtrack" -> FilmographicCategory.SOUNDTRACK
            else -> return@forEach
        }
        insertTitle(category, title)
    }
    return categoryMap
}

data class TitleInfo(
    val ratingValue: String?,
    val ratingBest: String?,
    val ratingCount: String?,
    val name: String,
    val yearReleased: String?,
    val duration: String?,
    val posterUrl: String?,
    val description: String?,
    val credits: List<Credit>,
    val cast: List<CastMember>,
) : MediaEntityInfo() {
    data class Credit(val type: String, val names: List<String>)
    data class CastMember(
        val image: String,
        val name: String,
        val role: String,
        val link: String,
    )
}

fun transformTitleInfo(title: TitleInfo): TitleDetails? = with(title) {
    TitleDetails(
        name,
        posterUrl?.let(::transformImageUrl),
        "",
        "",
        credits.getCreditStringMatching("Direct"),
        credits.getCreditStringMatching("Write"),
        credits.getCreditStringMatching("Creat"),
        description,
        yearReleased,
        transformTitleDetailsRating(ratingValue, ratingBest, ratingCount),
        duration,
        transformCast(cast),
    )
}

fun transformCast(cast: List<TitleInfo.CastMember>): List<TitleDetails.CastMember> =
    cast.mapNotNull {
        if (it.name.isBlank() && it.role.isBlank()) {
            null
        } else {
            TitleDetails.CastMember(
                it.image.ifBlank { null }
                    ?.let { ImageResizer.resize(it, ImageResizer.SIZE_WIDTH_THUMBNAIL) },
                it.name.ifBlank { null },
                it.role.ifBlank { null },
                it.link.ifBlank { null }?.let {
                    it.split("/")[2]
                }?.takeIf { it.startsWith("nm") }
            )
        }
    }

private fun List<TitleInfo.Credit>.getCreditStringMatching(partialType: String): String? =
    find { it.type.contains(partialType) }?.names?.joinToString(", ")

fun transformTitleDetailsRating(
    ratingValue: String?,
    ratingBest: String?,
    ratingCount: String?,
): TitleDetails.Rating? = ratingValue?.let {
    TitleDetails.Rating(
        ratingValue,
        ratingBest ?: "10",
        ratingCount,
    )
}
