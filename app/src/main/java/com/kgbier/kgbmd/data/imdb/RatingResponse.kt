package com.kgbier.kgbmd.data.imdb

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RatingResponse(
    @Json(name = "resource")
    val ratingInfo: RatingInfo
) {
    @JsonClass(generateAdapter = true)
    data class RatingInfo(
        val id: String,
        val title: String,
        val titleType: String,
        val year: Int,
        val bottomRank: Long,
        val rating: Float,
        val ratingCount: Long,
        val topRank: Int
    )
}