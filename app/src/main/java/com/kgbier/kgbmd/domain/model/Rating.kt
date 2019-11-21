package com.kgbier.kgbmd.domain.model

import com.kgbier.kgbmd.data.imdb.model.RatingResponse

fun transformRatingResponse(response: RatingResponse) = transformRatingInfo(response.ratingInfo)

fun transformRatingInfo(info: RatingResponse.RatingInfo) =
    info.rating?.takeIf { it > 0 }?.let { String.format("%.1f", it) }
