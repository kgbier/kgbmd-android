package com.kgbier.kgbmd.view.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kgbier.kgbmd.domain.model.TitleDetails
import com.kgbier.kgbmd.domain.repo.MediaInfoRepo
import com.kgbier.kgbmd.view.ui.titledetails.BaseTitlesViewModel
import com.kgbier.kgbmd.view.ui.titledetails.HeaderViewModel
import com.kgbier.kgbmd.view.ui.titledetails.TitledTextTitlesViewModel
import kotlinx.coroutines.launch

class TitleDetailsViewModel(titleId: String) : ViewModel() {

    sealed class TitleDetailsState {
        object Loading : TitleDetailsState()
        data class Error(val message: String) : TitleDetailsState()
        data class Loaded(val details: List<BaseTitlesViewModel>) : TitleDetailsState()
    }

    val titleDetails: MutableLiveData<TitleDetailsState> = MutableLiveData()
    val titleHeading: MutableLiveData<String> = MutableLiveData()

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

    private fun transformDetails(titleDetails: TitleDetails): List<BaseTitlesViewModel> {
        val list = mutableListOf<BaseTitlesViewModel>()
        with(titleDetails) {
            titleHeading.value = name

            list.add(HeaderViewModel(name, yearReleased, rating, poster))
            directedBy?.let { list.add(TitledTextTitlesViewModel("Directed by", it)) }
            writtenBy?.let { list.add(TitledTextTitlesViewModel("Written by", it)) }
            createdBy?.let { list.add(TitledTextTitlesViewModel("Created by", it)) }
            description?.let { list.add(TitledTextTitlesViewModel("Summary", it)) }
            // these are filler for now
            directedBy?.let { list.add(TitledTextTitlesViewModel("Directed by", it)) }
            writtenBy?.let { list.add(TitledTextTitlesViewModel("Written by", it)) }
            createdBy?.let { list.add(TitledTextTitlesViewModel("Created by", it)) }
            description?.let { list.add(TitledTextTitlesViewModel("Summary", it)) }
            directedBy?.let { list.add(TitledTextTitlesViewModel("Directed by", it)) }
            writtenBy?.let { list.add(TitledTextTitlesViewModel("Written by", it)) }
            createdBy?.let { list.add(TitledTextTitlesViewModel("Created by", it)) }
            description?.let { list.add(TitledTextTitlesViewModel("Summary", it)) }
            directedBy?.let { list.add(TitledTextTitlesViewModel("Directed by", it)) }
            writtenBy?.let { list.add(TitledTextTitlesViewModel("Written by", it)) }
            createdBy?.let { list.add(TitledTextTitlesViewModel("Created by", it)) }
            description?.let { list.add(TitledTextTitlesViewModel("Summary", it)) }
            directedBy?.let { list.add(TitledTextTitlesViewModel("Directed by", it)) }
            writtenBy?.let { list.add(TitledTextTitlesViewModel("Written by", it)) }
            createdBy?.let { list.add(TitledTextTitlesViewModel("Created by", it)) }
            description?.let { list.add(TitledTextTitlesViewModel("Summary", it)) }
        }
        return list
    }
}
