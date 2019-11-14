package com.kgbier.kgbmd.view.component

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProviders
import com.kgbier.kgbmd.MainActivity
import com.kgbier.kgbmd.util.bind
import com.kgbier.kgbmd.view.ui.SearchResultsView
import com.kgbier.kgbmd.view.viewmodel.MovieListSearchViewModel

@SuppressLint("ViewConstructor")
class SearchResults(context: MainActivity) : SearchResultsView(context) {

    private var movieListSearchViewModel: MovieListSearchViewModel =
        ViewModelProviders.of(context).get(MovieListSearchViewModel::class.java)

    init {
        movieListSearchViewModel.resultList.bind(context) {
            resultAdapter.submitList(it)
        }
    }
}