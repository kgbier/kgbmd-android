package com.kgbier.kgbmd.domain.repo

import com.kgbier.kgbmd.data.PreferencesService
import com.kgbier.kgbmd.domain.model.TitleCategory

object PreferencesRepo {
    fun getSavedTitleCategory() = PreferencesService.getMainTitleCategory()
    fun setSavedTitleCategory(titleCategory: TitleCategory) =
        PreferencesService.setMainTitleCategory(titleCategory)
}