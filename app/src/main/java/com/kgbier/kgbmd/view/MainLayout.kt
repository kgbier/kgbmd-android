package com.kgbier.kgbmd.view

import android.annotation.SuppressLint
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.updateLayoutParams
import androidx.core.view.updateMargins
import androidx.core.view.updatePadding
import androidx.lifecycle.ViewModelProviders
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.kgbier.kgbmd.MainActivity
import com.kgbier.kgbmd.util.bind
import com.kgbier.kgbmd.util.dp
import com.kgbier.kgbmd.util.setOnUpdateWithWindowInsetsListener
import com.kgbier.kgbmd.view.behaviour.ScrollBehaviour
import com.kgbier.kgbmd.view.component.ReadOnlySearchBar
import com.kgbier.kgbmd.view.component.TiledPosterGrid
import com.kgbier.kgbmd.view.viewmodel.MovieListViewModel

@SuppressLint("ViewConstructor")
class MainLayout(context: MainActivity) : CoordinatorLayout(context) {

    private val readOnlySearchBar: ReadOnlySearchBar

    private val swipeRefreshLayout: SwipeRefreshLayout
    private val tiledPosterGrid: TiledPosterGrid

    private var movieListViewModel: MovieListViewModel =
        ViewModelProviders.of(context).get(MovieListViewModel::class.java)

    private val WINDOW_MARGIN = dp(16)
    private val GRID_MARGIN_TOP = dp(40 + 32)
    private val GRID_MARGIN_BOTTOM = dp(8)

    private val PULLDOWN_END_DISTANCE = dp(64 + 64)

    init {
        // Setup Search Bar
        readOnlySearchBar = ReadOnlySearchBar(context).apply {
            layoutParams =
                LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT).apply {
                    marginStart = WINDOW_MARGIN
                    marginEnd = WINDOW_MARGIN
                    topMargin = WINDOW_MARGIN
                    behavior = ScrollBehaviour(context)
                }

            setOnUpdateWithWindowInsetsListener { _, insets, _, intendedMargin ->
                updateLayoutParams<MarginLayoutParams> {
                    updateMargins(top = intendedMargin.top + insets.systemWindowInsetTop)
                }
            }
        }.also(::addView)

        // Setup Movie List
        swipeRefreshLayout = SwipeRefreshLayout(context).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            setProgressViewEndTarget(false, PULLDOWN_END_DISTANCE)
            setOnRefreshListener { movieListViewModel.load() }

            setOnUpdateWithWindowInsetsListener { _, insets, _, _ ->
                setProgressViewEndTarget(false, PULLDOWN_END_DISTANCE + insets.systemWindowInsetTop)

                insets.consumeSystemWindowInsets()
            }

        }.also(::addView)

        tiledPosterGrid = TiledPosterGrid(context).apply {
            setPadding(
                dp(8),
                GRID_MARGIN_TOP,
                dp(8),
                GRID_MARGIN_BOTTOM
            )

            setOnUpdateWithWindowInsetsListener { _, insets, intendedPadding, _ ->
                updatePadding(
                    top = intendedPadding.top + insets.systemWindowInsetTop,
                    bottom = intendedPadding.bottom + insets.systemWindowInsetBottom
                )
                insets.consumeSystemWindowInsets()
            }
        }

        swipeRefreshLayout.addView(tiledPosterGrid)

        movieListViewModel.isLoading.bind(context) {
            if (!it) swipeRefreshLayout.isRefreshing = it
        }
    }

}

