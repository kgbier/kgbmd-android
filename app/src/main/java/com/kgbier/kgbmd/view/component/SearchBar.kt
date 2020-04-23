package com.kgbier.kgbmd.view.component

import android.annotation.SuppressLint
import androidx.activity.viewModels
import androidx.core.widget.doOnTextChanged
import com.kgbier.kgbmd.MainActivity
import com.kgbier.kgbmd.util.LiveDataDisposeBag
import com.kgbier.kgbmd.view.ui.SearchBarView
import com.kgbier.kgbmd.view.viewmodel.MovieListSearchViewModel

@SuppressLint("ViewConstructor")
class SearchBar(context: MainActivity) : SearchBarView(context) {

    private val disposeBag = LiveDataDisposeBag()

    private val movieListSearchViewModel: MovieListSearchViewModel by context.viewModels()

    init {
        editTextSearch.hint = movieListSearchViewModel.hint

        editTextSearch.doOnTextChanged { text, _, _, _ ->
            movieListSearchViewModel.onSearchQueryUpdated(text.toString())
        }

        movieListSearchViewModel.searchString.value?.let(editTextSearch::setText)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        disposeBag.dispose()
    }
}
