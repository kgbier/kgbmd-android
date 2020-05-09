package com.kgbier.kgbmd.view.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kgbier.kgbmd.domain.model.TitleDetails
import com.kgbier.kgbmd.domain.repo.MediaInfoRepo
import kotlinx.coroutines.launch

class TitleDetailsViewModel(titleId: String) : ViewModel() {

    sealed class TitleDetailsState {
        object Loading : TitleDetailsState()
        data class Error(val message: String) : TitleDetailsState()
        data class Loaded(val details: TitleDetails) : TitleDetailsState()
    }

    val titleDetails: MutableLiveData<TitleDetailsState> = MutableLiveData()

    init {
        load(titleId)
    }

    private fun load(titleId: String) = viewModelScope.launch {
        titleDetails.postValue(TitleDetailsState.Loading)
        runCatching {
            MediaInfoRepo.getMovieDetails(titleId) ?: throw Throwable()
        }.onSuccess {
            titleDetails.postValue(TitleDetailsState.Loaded(it))
        }.onFailure {
            titleDetails.postValue(TitleDetailsState.Error(it.message ?: "Error"))
        }
    }
}
