package com.kgbier.kgbmd.view.component

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProviders
import com.kgbier.kgbmd.MainActivity
import com.kgbier.kgbmd.util.bind
import com.kgbier.kgbmd.view.viewmodel.MovieListViewModel
import com.kgbier.kgbmd.view.ui.PosterLoadingAdapter
import com.kgbier.kgbmd.view.ui.TiledPosterView

@SuppressLint("ViewConstructor")
class TiledPosterGrid(context: MainActivity) : TiledPosterView(context) {

    private var movieListViewModel: MovieListViewModel =
        ViewModelProviders.of(context).get(MovieListViewModel::class.java)

    init {
        movieListViewModel.movieList.bind(context) {
            posterAdapter.submitList(it)
            if (adapter is PosterLoadingAdapter) {
                swapAdapter(posterAdapter, true)
            }
        }
    }
}