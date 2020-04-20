package com.kgbier.kgbmd.view.component

import android.annotation.SuppressLint
import androidx.activity.viewModels
import androidx.core.widget.doOnTextChanged
import com.kgbier.kgbmd.MainActivity
import com.kgbier.kgbmd.view.ui.SearchBarView
import com.kgbier.kgbmd.view.viewmodel.MovieListSearchViewModel

@SuppressLint("ViewConstructor")
class SearchBar(context: MainActivity) : SearchBarView(context) {

    private val movieListSearchViewModel: MovieListSearchViewModel by context.viewModels()

    init {
        editTextSearch.hint = movieListSearchViewModel.hint

        editTextSearch.doOnTextChanged { text, _, _, _ ->
            movieListSearchViewModel.search(text.toString())
        }
    }
}