package com.kgbier.kgbmd.view.component

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProviders
import com.kgbier.kgbmd.MainActivity
import com.kgbier.kgbmd.util.LiveDataDisposeBag
import com.kgbier.kgbmd.util.bind
import com.kgbier.kgbmd.util.disposeBy
import com.kgbier.kgbmd.view.ui.PosterLoadingAdapter
import com.kgbier.kgbmd.view.ui.TiledPosterView
import com.kgbier.kgbmd.view.viewmodel.MovieListViewModel

@SuppressLint("ViewConstructor")
class TiledPosterGrid(context: MainActivity) : TiledPosterView(context) {

    private val disposeBag = LiveDataDisposeBag()

    private var movieListViewModel: MovieListViewModel =
        ViewModelProviders.of(context).get(MovieListViewModel::class.java)

    init {
        movieListViewModel.movieList.bind(context) {
            posterAdapter.submitList(it)
            if (adapter is PosterLoadingAdapter) {
                swapAdapter(posterAdapter, true)
            }
        }.disposeBy(disposeBag)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        disposeBag.dispose()
    }
}