package com.kgbier.kgbmd.view.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kgbier.kgbmd.data.imdb.SuggestionResponse
import com.kgbier.kgbmd.data.network.ImdbService
import kotlinx.coroutines.launch

class MovieListSearchViewModel : ViewModel() {

    val hint = "Search movies, shows, actors"
    val isSearching: MutableLiveData<Boolean> = MutableLiveData()
    val resultList: MutableLiveData<List<SuggestionResponse.Result>> = MutableLiveData()

    fun search(query: String) = viewModelScope.launch {
        isSearching.postValue(true)
        try {
            val movies = ImdbService.search(query)
            Log.d("TAG", movies.first().toString())
            val rating = ImdbService.getRating(movies.first().ttid)
            resultList.postValue(movies)
        } catch (t: Throwable) {
        }
    }
}