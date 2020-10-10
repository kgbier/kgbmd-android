package com.kgbier.kgbmd.data.imdb.model

import com.kgbier.kgbmd.domain.imdb.operation.ImageResizer
import com.kgbier.kgbmd.domain.model.NameDetails
import com.kgbier.kgbmd.domain.model.TitleDetails

sealed class MediaEntityInfo

data class NameInfo(
    val name: String,
    val headshotUrl: String?,
    val description: String?,
    val filmography: List<Title>,
) : MediaEntityInfo() {
    data class Title(val name: String, val role: String)
}

fun transformNameInfo(nameInfo: NameInfo): NameDetails? = with(nameInfo) {
    NameDetails(
        name,
        headshotUrl?.let(::transformImageUrl),
        description,
        transformFilmography(filmography),
    )
}

fun transformFilmography(title: List<NameInfo.Title>): List<NameDetails.Title> = title.mapNotNull {
    if (it.name.isBlank() && it.role.isBlank()) {
        null
    } else {
        NameDetails.Title(
            it.name.ifBlank { null },
            it.role.ifBlank { null },
        )
    }
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
    data class CastMember(val image: String, val name: String, val role: String)
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
