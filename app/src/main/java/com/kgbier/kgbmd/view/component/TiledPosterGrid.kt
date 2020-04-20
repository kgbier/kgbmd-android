package com.kgbier.kgbmd.view.component

import android.annotation.SuppressLint
import androidx.activity.viewModels
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

    private val movieListViewModel: MovieListViewModel by context.viewModels()

    init {
        movieListViewModel.titleList.bind(context) {
            if (it is MovieListViewModel.TitleListState.Loaded) {
                posterAdapter.submitList(it.items)
                if (adapter is PosterLoadingAdapter) {
                    swapAdapter(posterAdapter, true)
                }
            }
        }.disposeBy(disposeBag)

//        posterAdapter.onItemClickListener = { position ->
//            context.navigate(Route.DetailScreen)
//        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        disposeBag.dispose()
    }
}