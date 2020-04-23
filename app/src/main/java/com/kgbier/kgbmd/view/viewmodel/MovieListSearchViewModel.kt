package com.kgbier.kgbmd.view.viewmodel

import androidx.lifecycle.*
import com.kgbier.kgbmd.domain.model.SearchSuggestionType
import com.kgbier.kgbmd.domain.model.Suggestion
import com.kgbier.kgbmd.domain.repo.MediaInfoRepo
import kotlinx.coroutines.launch

sealed class RatingResult {
    object Loading : RatingResult()
    data class Result(val rating: String?) : RatingResult()
}

class MovieListSearchViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    companion object {
        const val SEARCH_STRING_HANDLE = "search-string"
    }

    val hint = "Search movies, shows, actors"
    val searchString: MutableLiveData<String> = savedStateHandle.getLiveData(SEARCH_STRING_HANDLE)
    val isFirstLoad: MutableLiveData<Boolean> = MutableLiveData()
    val resultList: MutableLiveData<List<Suggestion>?> = MutableLiveData()

    private val ratingResults = mutableMapOf<String, MutableLiveData<RatingResult>>()

    fun liveRatingResult(id: String, type: SearchSuggestionType?): LiveData<RatingResult>? =
        when (type) {
            SearchSuggestionType.MOVIE -> ratingResults[id] ?: fetchNewLiveResult(id)
            else -> null
        }

    private fun fetchNewLiveResult(id: String): LiveData<RatingResult> =
        MutableLiveData<RatingResult>().apply {
            viewModelScope.launch {
                postValue(RatingResult.Loading)
                postValue(RatingResult.Result(MediaInfoRepo.getRating(id)))
            }
        }.also { ratingResults[id] = it }

    fun onSearchQueryUpdated(query: String) {
        if (query != searchString.value) {
            searchString.value = query
        }
        search(query)
    }

    private fun search(query: String) = viewModelScope.launch {
        if (query.isBlank()) {
            clearSearchState()
            return@launch
        }
        if (isFirstLoad.value != true) isFirstLoad.postValue(true)

        runCatching {
            MediaInfoRepo.getSearchResults(query)
        }.onSuccess {
            resultList.postValue(it)
        }
    }

    fun clearSearchState() {
        searchString.value = ""
        resultList.postValue(null)
        isFirstLoad.postValue(false)
    }
}
