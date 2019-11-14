package com.kgbier.kgbmd.domain

import com.kgbier.kgbmd.data.network.ImdbService

object ImdbRepo {
    suspend fun getMovieHotListPosters(): List<MoviePoster> =
        ImdbService.getHotMovies().map(::transformHotListItem)

    suspend fun getSearchResults(query: String): List<SearchSuggestion> =
        ImdbService.search(query).run(::transformSuggestionResponse)
}