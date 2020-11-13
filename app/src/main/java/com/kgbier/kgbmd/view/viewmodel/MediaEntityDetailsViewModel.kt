package com.kgbier.kgbmd.view.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kgbier.kgbmd.domain.model.FilmographicCategory
import com.kgbier.kgbmd.domain.model.MediaEntityDetails
import com.kgbier.kgbmd.domain.model.NameDetails
import com.kgbier.kgbmd.domain.model.TitleDetails
import com.kgbier.kgbmd.domain.repo.MediaInfoRepo
import com.kgbier.kgbmd.view.ui.mediaentitydetails.*
import kotlinx.coroutines.launch

class MediaEntityDetailsViewModel(entityId: String) : ViewModel() {

    sealed class DetailsState {
        object Loading : DetailsState()
        data class Error(val message: String) : DetailsState()
        data class Loaded(val details: List<BaseMediaEntityListItemViewModel>) : DetailsState()
    }

    val state: MutableLiveData<DetailsState> = MutableLiveData()
    val heading: MutableLiveData<String> = MutableLiveData()

    init {
        load(entityId)
    }

    private fun load(entityId: String) = viewModelScope.launch {
        state.postValue(DetailsState.Loading)
        runCatching {
            val details = MediaInfoRepo.getMediaEntityDetails(entityId) ?: throw Throwable()
            transformDetails(details)
        }.onSuccess {
            state.postValue(DetailsState.Loaded(it))
        }.onFailure {
            state.postValue(DetailsState.Error(it.message ?: "Error"))
        }
    }

    private fun transformDetails(entityDetails: MediaEntityDetails): List<BaseMediaEntityListItemViewModel> =
        when (entityDetails) {
            is TitleDetails -> transformTitleDetails(entityDetails)
            is NameDetails -> transformNameDetails(entityDetails)
        }

    private fun transformTitleDetails(titleDetails: TitleDetails): List<BaseMediaEntityListItemViewModel> {
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
                list.add(CastMemberViewModel(it.name ?: "", it.role, it.thumbnailUrl, it.nameId))
            }
        }
        return list
    }

    private fun transformNameDetails(nameDetails: NameDetails): List<BaseMediaEntityListItemViewModel> {
        val list = mutableListOf<BaseMediaEntityListItemViewModel>()
        fun buildFilmographySection(heading: String, titles: List<NameDetails.Title>) {
            list.add(SectionHeadingViewModel(heading))
            titles.forEach {
                list.add(FilmographyViewModel(it.name ?: "", it.year, it.role, null))
            }
        }
        with(nameDetails) {
            heading.value = name

            list.add(HeaderViewModel(name, null, null, headshot))
            filmography[FilmographicCategory.ACTOR]?.let {
                buildFilmographySection("Actor", it)
            }
            filmography[FilmographicCategory.ACTRESS]?.let {
                buildFilmographySection("Actress", it)
            }
            filmography[FilmographicCategory.DIRECTOR]?.let {
                buildFilmographySection("Director", it)
            }
            filmography[FilmographicCategory.COMPOSER]?.let {
                buildFilmographySection("Composer", it)
            }
        }
        return list
    }
}
