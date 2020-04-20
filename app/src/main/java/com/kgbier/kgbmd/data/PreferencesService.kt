package com.kgbier.kgbmd.data

import android.content.Context
import com.kgbier.kgbmd.MainApplication
import com.kgbier.kgbmd.domain.model.TitleCategory

object PreferencesService {

    private const val PREFERENCES_FILE_NAME = "app-preferences"
    private val sharedPreferences by lazy {
        MainApplication.applicationContext.getSharedPreferences(
            PREFERENCES_FILE_NAME, Context.MODE_PRIVATE
        )
    }

    /**
     * Main Title Category
     * Possible values:
     *  0: Movie (default)
     *  1: TV Show
     */
    private const val TITLE_CATEGORY = "maintitlecategory"

    fun getMainTitleCategory(): TitleCategory = sharedPreferences.getInt(TITLE_CATEGORY, -1).let {
        when (it) {
            1 -> TitleCategory.TV_SHOW
            else -> TitleCategory.MOVIE
        }
    }

    fun setMainTitleCategory(titleCategory: TitleCategory) {
        val value = when (titleCategory) {
            TitleCategory.MOVIE -> 0
            TitleCategory.TV_SHOW -> 1
        }
        sharedPreferences.edit().putInt(TITLE_CATEGORY, value).apply()
    }
}