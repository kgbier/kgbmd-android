package com.kgbier.kgbmd.view.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kgbier.kgbmd.domain.model.TitleDetails
import com.kgbier.kgbmd.domain.repo.MediaInfoRepo
import com.kgbier.kgbmd.view.ui.mediaentitydetails.*
import kotlinx.coroutines.launch

class MediaEntityDetailsViewModel(titleId: String) : ViewModel() {

    sealed class DetailsState {
        object Loading : DetailsState()
        data class Error(val message: String) : DetailsState()
        data class Loaded(val details: List<BaseMediaEntityListItemViewModel>) : DetailsState()
    }

    val state: MutableLiveData<DetailsState> = MutableLiveData()
    val heading: MutableLiveData<String> = MutableLiveData()

    init {
        load(titleId)
    }

    private fun load(titleId: String) = viewModelScope.launch {
        state.postValue(DetailsState.Loading)
        runCatching {
            MediaInfoRepo.getTitleDetails(titleId) ?: throw Throwable()
        }.onSuccess {
            state.postValue(DetailsState.Loaded(transformDetails(it)))
        }.onFailure {
            state.postValue(DetailsState.Error(it.message ?: "Error"))
        }
    }

    private fun transformDetails(titleDetails: TitleDetails): List<BaseMediaEntityListItemViewModel> {
        val list = mutableListOf<BaseMediaEntityListItemViewModel>()
        with(titleDetails) {
            heading.value = name

            list.add(HeaderViewModel(name, yearReleased, rating, poster))
            directedBy?.let { list.add(TitledTextViewModel("Directed by", it)) }
            writtenBy?.let { list.add(TitledTextViewModel("Written by", it)) }
            createdBy?.let { list.add(TitledTextViewModel("Created by", it)) }
            description?.let { list.add(TitledTextViewModel("Summary", it)) }

            if (castMembers.isNotEmpty()) {
                list.add(SectionHeadingViewModel("Cast"))
            }
            castMembers.forEach {
                list.add(CastMemberViewModel(it.name ?: "", it.role, it.thumbnailUrl))
            }
        }
        return list
    }
}
