package com.kgbier.kgbmd.data.imdb.model

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
    val credits: List<Credit>
) {
    data class Credit(val type: String, val names: List<String>)
}

fun transformTitleInfo(title: TitleInfo): TitleDetails? = with(title) {
    TitleDetails(
        name.trim().trimTrailingNbsp(),
        transformTitleDetailsPoster(posterUrl),
        "",
        "",
        credits.getCreditStringMatching("Direct"),
        credits.getCreditStringMatching("Write"),
        credits.getCreditStringMatching("Creat"),
        description?.trim(),
        yearReleased,
        transformTitleDetailsRating(ratingValue, ratingBest, ratingCount),
        duration
    )
}

private fun String.trimTrailingNbsp(): String {
    if (indexOf("&n") == -1) return this
    return replace("&nbsp;", "")
}

private fun List<TitleInfo.Credit>.getCreditStringMatching(partialType: String): String? =
    find { it.type.contains(partialType) }?.names?.joinToString(", ") { it.trim() }

fun transformTitleDetailsPoster(posterUrl: String?): TitleDetails.Poster? = posterUrl?.let {
    TitleDetails.Poster(it, it)
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