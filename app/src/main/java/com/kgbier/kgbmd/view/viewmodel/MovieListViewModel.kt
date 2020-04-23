package com.kgbier.kgbmd.view.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kgbier.kgbmd.domain.model.MoviePoster
import com.kgbier.kgbmd.domain.model.TitleCategory
import com.kgbier.kgbmd.domain.repo.ImdbRepo
import com.kgbier.kgbmd.domain.repo.PreferencesRepo
import kotlinx.coroutines.launch

class MovieListViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    companion object {
        const val TITLE_CATEGORY_HANDLE = "title-category"
    }

    sealed class TitleListState {
        object Loading : TitleListState()
        data class Loaded(val items: List<MoviePoster>) : TitleListState()
    }

    val titleCategory: MutableLiveData<TitleCategory> =
        savedStateHandle.getLiveData(TITLE_CATEGORY_HANDLE, PreferencesRepo.getSavedTitleCategory())
    val isSpinnerShown: MutableLiveData<Boolean> = MutableLiveData()
    val titleList: MutableLiveData<TitleListState> = MutableLiveData(TitleListState.Loading)

    init {
        load(titleCategory.value!!)
    }

    fun reload() = load(titleCategory.value!!)

    private fun load(titleCategory: TitleCategory) = viewModelScope.launch {
        runCatching {
            when (titleCategory) {
                TitleCategory.MOVIE -> titleList.postValue(TitleListState.Loaded(ImdbRepo.getMovieHotListPosters()))
                TitleCategory.TV_SHOW -> titleList.postValue(TitleListState.Loaded(ImdbRepo.getTvShowHotListPosters()))
            }
        }
        isSpinnerShown.postValue(false)
    }

    fun toggleTitleCategory() {
        val newCategory = when (titleCategory.value!!) {
            TitleCategory.MOVIE -> TitleCategory.TV_SHOW
            TitleCategory.TV_SHOW -> TitleCategory.MOVIE
        }

        isSpinnerShown.value = true
        titleList.value = TitleListState.Loading
        load(newCategory)

        PreferencesRepo.setSavedTitleCategory(newCategory)
        titleCategory.value = newCategory
    }
}