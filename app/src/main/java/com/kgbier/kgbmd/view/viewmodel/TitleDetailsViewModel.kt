package com.kgbier.kgbmd.view.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kgbier.kgbmd.domain.model.TitleDetails
import com.kgbier.kgbmd.domain.repo.MediaInfoRepo
import com.kgbier.kgbmd.view.ui.listingdetails.BaseListingViewModel
import com.kgbier.kgbmd.view.ui.listingdetails.HeaderViewModel
import com.kgbier.kgbmd.view.ui.listingdetails.TitledTextListingViewModel
import kotlinx.coroutines.launch

class TitleDetailsViewModel(titleId: String) : ViewModel() {

    sealed class TitleDetailsState {
        object Loading : TitleDetailsState()
        data class Error(val message: String) : TitleDetailsState()
        data class Loaded(val details: List<BaseListingViewModel>) : TitleDetailsState()
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
            titleDetails.postValue(TitleDetailsState.Loaded(transformDetails(it)))
        }.onFailure {
            titleDetails.postValue(TitleDetailsState.Error(it.message ?: "Error"))
        }
    }

    private fun transformDetails(titleDetails: TitleDetails): List<BaseListingViewModel> {
        val list = mutableListOf<BaseListingViewModel>()
        with(titleDetails) {
            list.add(HeaderViewModel(name, yearReleased, rating, poster))
            directedBy?.let { list.add(TitledTextListingViewModel("Directed by", it)) }
            writtenBy?.let { list.add(TitledTextListingViewModel("Written by", it)) }
            createdBy?.let { list.add(TitledTextListingViewModel("Created by", it)) }
            description?.let { list.add(TitledTextListingViewModel("Summary", it)) }
        }
        return list
    }
}
