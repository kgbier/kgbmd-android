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

        movieListSearchViewModel.clearSearchState()

        movieListSearchViewModel.isFirstLoad.bind(context) {
            if (it) {
                visibility = View.VISIBLE
                emptyStateMessage.visibility = View.GONE
                loadingProgressBar.visibility = View.VISIBLE
            }
        }

        movieListSearchViewModel.resultList.bind(context) {

            if (it == null) {
                visibility = View.GONE
                emptyStateMessage.visibility = View.GONE
                loadingProgressBar.visibility = View.GONE
                resultAdapter.submitList(null)

                return@bind
            }

            resultAdapter.submitList(it)
            loadingProgressBar.visibility = View.GONE
            if (it.isEmpty()) {
                emptyStateMessage.visibility = View.VISIBLE
            } else {
                emptyStateMessage.visibility = View.GONE
            }
        }
    }
}