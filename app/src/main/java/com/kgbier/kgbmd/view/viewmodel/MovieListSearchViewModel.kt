package com.kgbier.kgbmd.view.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kgbier.kgbmd.domain.repo.ImdbRepo
import com.kgbier.kgbmd.domain.model.SearchSuggestion
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