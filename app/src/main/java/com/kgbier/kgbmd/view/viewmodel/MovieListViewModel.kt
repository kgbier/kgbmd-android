package com.kgbier.kgbmd.view.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kgbier.kgbmd.domain.model.MoviePoster
import com.kgbier.kgbmd.domain.repo.ImdbRepo
import kotlinx.coroutines.launch

class MovieListViewModel : ViewModel() {

    enum class TitleCategory {
        MOVIE,
        TV
    }

    sealed class TitleListState {
        object Loading : TitleListState()
        data class Loaded(val items: List<MoviePoster>) : TitleListState()
    }

    val titleCategory: MutableLiveData<TitleCategory> = MutableLiveData(TitleCategory.MOVIE)
    val isSpinnerShown: MutableLiveData<Boolean> = MutableLiveData()
    val titleList: MutableLiveData<TitleListState> = MutableLiveData(TitleListState.Loading)

    init {
        load(TitleCategory.MOVIE)
    }

    fun reload() = load(titleCategory.value!!)

    fun load(titleCategory: TitleCategory) = viewModelScope.launch {
        try {
            when (titleCategory) {
                TitleCategory.MOVIE -> titleList.postValue(TitleListState.Loaded(ImdbRepo.getMovieHotListPosters()))
                TitleCategory.TV -> titleList.postValue(TitleListState.Loaded(ImdbRepo.getTvShowHotListPosters()))
            }
        } catch (t: Throwable) {
        } finally {
            isSpinnerShown.postValue(false)
        }
    }

    fun toggleTitleCategory() {
        val newCategory = when (titleCategory.value!!) {
            TitleCategory.MOVIE -> TitleCategory.TV
            TitleCategory.TV -> TitleCategory.MOVIE
        }
        isSpinnerShown.postValue(true)
        titleList.postValue(TitleListState.Loading)
        load(newCategory)
        titleCategory.value = newCategory
    }
}