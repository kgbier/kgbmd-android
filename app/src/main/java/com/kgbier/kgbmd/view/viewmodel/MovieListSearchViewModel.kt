package com.kgbier.kgbmd.view.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kgbier.kgbmd.domain.ImdbRepo
import com.kgbier.kgbmd.domain.SearchSuggestion
import kotlinx.coroutines.launch

class MovieListSearchViewModel : ViewModel() {

    val hint = "Search movies, shows, actors"

    val isFirstLoad: MutableLiveData<Boolean> = MutableLiveData()
    val resultList: MutableLiveData<List<SearchSuggestion>?> = MutableLiveData()

    fun search(query: String) = viewModelScope.launch {
        if (query.isBlank()) {
            clearSearchState()
            return@launch
        }
        if (!isFirstLoad.value!!) isFirstLoad.postValue(true)
        try {
            val movies = ImdbRepo.getSearchResults(query)
            resultList.postValue(movies)
        } catch (t: Throwable) {
        }
    }

    fun clearSearchState() {
        resultList.postValue(null)
        isFirstLoad.postValue(false)
    }
}