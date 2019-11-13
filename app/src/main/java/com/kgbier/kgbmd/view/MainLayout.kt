package com.kgbier.kgbmd.view

import android.annotation.SuppressLint
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.ViewModelProviders
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.kgbier.kgbmd.MainActivity
import com.kgbier.kgbmd.util.bind
import com.kgbier.kgbmd.util.dp
import com.kgbier.kgbmd.view.behaviour.ScrollBehaviour
import com.kgbier.kgbmd.view.component.SearchBar
import com.kgbier.kgbmd.view.component.TiledPosterGrid
import com.kgbier.kgbmd.view.viewmodel.MovieListViewModel

@SuppressLint("ViewConstructor")
class MainLayout(context: MainActivity) : CoordinatorLayout(context) {

    private val searchBar: SearchBar

    private val swipeRefreshLayout: SwipeRefreshLayout
    private val tiledPosterGrid: TiledPosterGrid

    private var movieListViewModel: MovieListViewModel =
        ViewModelProviders.of(context).get(MovieListViewModel::class.java)

    init {

        // Setup Search Bar
        searchBar = SearchBar(context).apply {
            layoutParams =
                LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT).apply {
                    marginStart = dp(16)
                    marginEnd = dp(16)
                    topMargin = dp(16)
                    behavior = ScrollBehaviour(context)
                }
        }.also(::addView)

        // Setup Movie List
        swipeRefreshLayout = SwipeRefreshLayout(context).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            setProgressViewEndTarget(false, dp(64 + 64))
            setOnRefreshListener { movieListViewModel.load() }
        }.also(::addView)

        tiledPosterGrid = TiledPosterGrid(context).apply {
            setPadding(dp(8), dp(40 + 24), dp(8), dp(8))
        }

        swipeRefreshLayout.addView(tiledPosterGrid)

        movieListViewModel.isLoading.bind(context) {
            if (!it) swipeRefreshLayout.isRefreshing = it
        }
    }
}

