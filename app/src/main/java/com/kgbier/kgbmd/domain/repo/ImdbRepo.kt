package com.kgbier.kgbmd.domain.repo

import com.kgbier.kgbmd.data.imdb.ImdbService
import com.kgbier.kgbmd.domain.model.MoviePoster
import com.kgbier.kgbmd.domain.model.SearchSuggestion
import com.kgbier.kgbmd.domain.model.transformHotListItem
import com.kgbier.kgbmd.domain.model.transformSuggestionResponse

object ImdbRepo {
    suspend fun getMovieHotListPosters(): List<MoviePoster> =
        ImdbService.getHotMovies().map(::transformHotListItem)

    suspend fun getSearchResults(query: String): List<SearchSuggestion> =
        ImdbService.search(query).run(::transformSuggestionResponse)
}