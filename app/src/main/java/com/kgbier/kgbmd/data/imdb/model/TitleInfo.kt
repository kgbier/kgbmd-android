package com.kgbier.kgbmd.data.imdb.model

import com.kgbier.kgbmd.domain.imdb.operation.ImageResizer
import com.kgbier.kgbmd.domain.model.TitleDetails

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
    val cast: List<CastMember>
) {
    data class Credit(val type: String, val names: List<String>)
    data class CastMember(val image: String, val name: String?, val role: String?)
}

fun transformTitleInfo(title: TitleInfo): TitleDetails? = with(title) {
    TitleDetails(
        name,
        transformTitleDetailsPoster(posterUrl),
        "",
        "",
        credits.getCreditStringMatching("Direct"),
        credits.getCreditStringMatching("Write"),
        credits.getCreditStringMatching("Creat"),
        description,
        yearReleased,
        transformTitleDetailsRating(ratingValue, ratingBest, ratingCount),
        duration,
        transformCast(cast)
    )
}

fun transformCast(cast: List<TitleInfo.CastMember>): List<TitleDetails.CastMember> = cast.map {
    TitleDetails.CastMember(
        it.image.ifEmpty { null },
        it.name,
        it.role
    )
}

private fun List<TitleInfo.Credit>.getCreditStringMatching(partialType: String): String? =
    find { it.type.contains(partialType) }?.names?.joinToString(", ")

fun transformTitleDetailsPoster(posterUrl: String?): TitleDetails.Poster? = posterUrl?.let {
    TitleDetails.Poster(
        ImageResizer.resize(it, ImageResizer.SIZE_WIDTH_HINT),
        ImageResizer.resize(it, ImageResizer.SIZE_WIDTH_LARGE)
    )
}

fun transformTitleDetailsRating(
    ratingValue: String?,
    ratingBest: String?,
    ratingCount: String?
): TitleDetails.Rating? = ratingValue?.let {
    TitleDetails.Rating(
        ratingValue,
        ratingBest ?: "10",
        ratingCount
    )
}
