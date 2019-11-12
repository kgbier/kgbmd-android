package com.kgbier.kgbmd.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kgbier.kgbmd.data.network.ImdbService
import com.kgbier.kgbmd.data.parse.HotListItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class MovieListViewModel : ViewModel(), CoroutineScope by MainScope() {

    val isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val movieList: MutableLiveData<List<HotListItem>> = MutableLiveData()

    init {
        load()
    }

    fun load() = launch {
        try {
            val movies = ImdbService.getHotMovies()
            movieList.postValue(movies)
            //tiledPosterView.setMovies(movies)
        } catch (t: Throwable) {
        } finally {
            isLoading.postValue(false)
        }
    }

    override fun onCleared() {
        cancel()
    }
}