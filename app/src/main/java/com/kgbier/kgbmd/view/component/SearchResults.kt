package com.kgbier.kgbmd.view.component

import android.annotation.SuppressLint
import android.view.View
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
        visibility = View.GONE
        emptyStateMessage.visibility = View.GONE

        movieListSearchViewModel.clear()

        movieListSearchViewModel.isFirstLoad.bind(context) {
            if (it) {
                visibility = View.VISIBLE
                emptyStateMessage.visibility = View.GONE
                loadingProgressBar.visibility = View.VISIBLE
            }
        }

        movieListSearchViewModel.resultList.bind(context) {
            loadingProgressBar.visibility = View.GONE
            it?.takeIf { it.isNotEmpty() }?.let {
                resultAdapter.submitList(it)
                emptyStateMessage.visibility = View.GONE
            } ?: run {
                resultAdapter.submitList(null)
                emptyStateMessage.visibility = View.VISIBLE
            }
        }
    }
}