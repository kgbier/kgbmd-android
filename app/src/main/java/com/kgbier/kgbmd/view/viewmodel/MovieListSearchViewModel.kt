package com.kgbier.kgbmd.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kgbier.kgbmd.domain.model.SearchSuggestionType
import com.kgbier.kgbmd.domain.model.Suggestion
import com.kgbier.kgbmd.domain.repo.ImdbRepo
import kotlinx.coroutines.launch

class MovieListSearchViewModel : ViewModel() {

    val hint = "Search movies, shows, actors"

    val isFirstLoad: MutableLiveData<Boolean> = MutableLiveData()
    val resultList: MutableLiveData<List<Suggestion>?> = MutableLiveData()

    private val ratingResults = mutableMapOf<String, MutableLiveData<String?>>()

    fun liveRatingResult(id: String, type: SearchSuggestionType?): LiveData<String?>? =
        when (type) {
            SearchSuggestionType.MOVIE -> ratingResults[id] ?: fetchNewLiveResult(id)
            else -> null
        }

    private fun fetchNewLiveResult(id: String): LiveData<String?>? =
        MutableLiveData<String?>().apply {
            viewModelScope.launch {
                postValue(ImdbRepo.getRating(id))
            }
        }.also { ratingResults[id] = it }

    fun search(query: String) = viewModelScope.launch {
        if (query.isBlank()) {
            clearSearchState()
            return@launch
        }
        if (!isFirstLoad.value!!) isFirstLoad.postValue(true)
        try {
            val results = ImdbRepo.getSearchResults(query)
            resultList.postValue(results)
        } catch (t: Throwable) {
        }
    }

    fun clearSearchState() {
        resultList.postValue(null)
        isFirstLoad.postValue(false)
    }
}