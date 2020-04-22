package com.kgbier.kgbmd.view.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kgbier.kgbmd.domain.model.MovieDetails
import com.kgbier.kgbmd.domain.repo.ImdbRepo
import kotlinx.coroutines.launch

class TitleDetailsViewModel : ViewModel() {

    sealed class TitleDetailsState {
        object Loading : TitleDetailsState()
        object Error : TitleDetailsState()
        data class Loaded(val details: MovieDetails) : TitleDetailsState()
    }

    val titleDetails: MutableLiveData<TitleDetailsState> = MutableLiveData()

    init {
        load("tt2527336")
    }

    private fun load(titleId: String) = viewModelScope.launch {
        titleDetails.postValue(TitleDetailsState.Loading)
        runCatching {
            ImdbRepo.getMovieDetails(titleId) ?: throw Throwable()
        }.onSuccess {
            titleDetails.postValue(TitleDetailsState.Loaded(it))
        }.onFailure {
            titleDetails.postValue(TitleDetailsState.Error)
        }
    }
}