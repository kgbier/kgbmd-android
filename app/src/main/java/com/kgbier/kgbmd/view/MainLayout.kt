package com.kgbier.kgbmd.view

import android.annotation.SuppressLint
import android.widget.FrameLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProviders
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.kgbier.kgbmd.MainActivity
import com.kgbier.kgbmd.util.bind
import com.kgbier.kgbmd.util.dp
import com.kgbier.kgbmd.view.ui.SearchBarView
import com.kgbier.kgbmd.view.ui.TiledPosterView

@SuppressLint("ViewConstructor")
class MainLayout(context: MainActivity) : FrameLayout(context) {

    private val searchBarView: SearchBarView

    private val swipeRefreshLayout: SwipeRefreshLayout
    private val tiledPosterView: TiledPosterView

    private var movieListViewModel: MovieListViewModel =
        ViewModelProviders.of(context).get(MovieListViewModel::class.java)

    init {

        // Setup Search Bar
        searchBarView = SearchBarView(context).apply {
            layoutParams =
                MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT).apply {
                    marginStart = dp(16)
                    marginEnd = dp(16)
                    topMargin = dp(16)
                }
        }.also(::addView)

        // Setup Movie List
        swipeRefreshLayout = SwipeRefreshLayout(context).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            setProgressViewEndTarget(false, dp(64 + 64))
            setOnRefreshListener { movieListViewModel.load() }
        }.also(::addView)

        tiledPosterView = TiledPosterView(context).apply {
            setPadding(0, dp(64 + 8), 0, 0)
        }

        swipeRefreshLayout.addView(tiledPosterView)

        // Start View Models
        startMovieListViewModelObservers(context)
    }

    private fun startMovieListViewModelObservers(lifecycleOwner: LifecycleOwner) {
        movieListViewModel.isLoading.bind(lifecycleOwner) {
            if (!it) swipeRefreshLayout.isRefreshing = it
        }

        movieListViewModel.movieList.bind(lifecycleOwner) {
            tiledPosterView.setMovies(it)
        }
    }
}

