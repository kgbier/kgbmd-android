package com.kgbier.kgbmd.view.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kgbier.kgbmd.domain.model.MoviePoster
import com.kgbier.kgbmd.domain.repo.ImdbRepo
import kotlinx.coroutines.launch

class MovieListViewModel : ViewModel() {

    val isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val movieList: MutableLiveData<List<MoviePoster>> = MutableLiveData()

    init {
        load()
    }

    fun load() = viewModelScope.launch {
        try {
            val movies = ImdbRepo.getMovieHotListPosters()
            movieList.postValue(movies)
        } catch (t: Throwable) {
        } finally {
            isLoading.postValue(false)
        }
    }
}