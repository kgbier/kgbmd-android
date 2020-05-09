package com.kgbier.kgbmd.domain.repo

import com.kgbier.kgbmd.data.imdb.ImdbService
import com.kgbier.kgbmd.data.imdb.model.transformSuggestionResponse
import com.kgbier.kgbmd.data.imdb.model.transformTitleInfo
import com.kgbier.kgbmd.domain.model.*

object MediaInfoRepo {
    suspend fun getMovieHotListPosters(): List<MoviePoster> =
        ImdbService.getHotMovies().map(::transformHotListItem)

    suspend fun getTvShowHotListPosters(): List<MoviePoster> =
        ImdbService.getHotShows().map(::transformHotListItem)

    suspend fun getSearchResults(query: String): List<Suggestion> =
        ImdbService.search(query).run(::transformSuggestionResponse)

    suspend fun getRating(id: String): String? =
        ImdbService.getRating(id)?.run(::transformRatingResponse)

    suspend fun getMovieDetails(id: String): TitleDetails? =
        ImdbService.getMovieDetails(id)?.run(::transformTitleInfo)
}
