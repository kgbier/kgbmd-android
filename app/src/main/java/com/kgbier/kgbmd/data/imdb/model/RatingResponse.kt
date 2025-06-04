package com.kgbier.kgbmd.data.imdb.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RatingResponse(
    @SerialName("resource")
    val ratingInfo: RatingInfo
) {
    @Serializable
    data class RatingInfo(
        val id: String,
        val title: String,
        val titleType: String,
        val year: Int?,
        val bottomRank: Long?,
        val rating: Float?,
        val ratingCount: Long?,
        val topRank: Int?
    )
}
