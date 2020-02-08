package com.kgbier.kgbmd.view.component

import android.annotation.SuppressLint
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.kgbier.kgbmd.MainActivity
import com.kgbier.kgbmd.view.ui.SearchBarView
import com.kgbier.kgbmd.view.viewmodel.MovieListSearchViewModel

@SuppressLint("ViewConstructor")
class SearchBar(context: MainActivity) : SearchBarView(context) {

    private var movieListSearchViewModel: MovieListSearchViewModel =
        ViewModelProvider(context)[MovieListSearchViewModel::class.java]

    init {
        editTextSearch.hint = movieListSearchViewModel.hint

        editTextSearch.doOnTextChanged { text, _, _, _ ->
            movieListSearchViewModel.search(text.toString())
        }
    }
}