package com.kgbier.kgbmd.view

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kgbier.kgbmd.data.imdb.SuggestionResponse
import com.kgbier.kgbmd.data.network.ImdbService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class MovieListSearchViewModel : ViewModel(), CoroutineScope by MainScope() {

    val isSearching: MutableLiveData<Boolean> = MutableLiveData()
    val resultList: MutableLiveData<List<SuggestionResponse.Result>> = MutableLiveData()

    fun search(query: String) = launch {
        isSearching.postValue(true)
        try {
            val movies = ImdbService.search(query)
            Log.d("TAG", movies.first().toString())
            val rating = ImdbService.getRating(movies.first().ttid)
            resultList.postValue(movies)
        } catch (t: Throwable) {
        }
    }

    override fun onCleared() {
        cancel()
    }
}