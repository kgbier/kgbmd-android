package com.kgbier.kgbmd.view

import android.annotation.SuppressLint
import android.transition.ChangeBounds
import android.transition.Slide
import android.transition.Transition
import android.transition.TransitionSet
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.updateLayoutParams
import androidx.core.view.updateMargins
import androidx.core.view.updatePadding
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.kgbier.kgbmd.MainActivity
import com.kgbier.kgbmd.R
import com.kgbier.kgbmd.TransitionRoute
import com.kgbier.kgbmd.domain.model.TitleCategory
import com.kgbier.kgbmd.util.*
import com.kgbier.kgbmd.view.animation.CornerRadiusTransition
import com.kgbier.kgbmd.view.behaviour.ScrollBehaviour
import com.kgbier.kgbmd.view.component.ReadOnlySearchBar
import com.kgbier.kgbmd.view.component.TiledPosterGrid
import com.kgbier.kgbmd.view.viewmodel.MovieListViewModel

@SuppressLint("ViewConstructor")
class MainLayout(context: MainActivity) : CoordinatorLayout(context) {

    private val disposeBag = LiveDataDisposeBag()

    private val readOnlySearchBar: ReadOnlySearchBar
    private val titleCategoryToggle: ImageView

    private val swipeRefreshLayout: SwipeRefreshLayout
    private val tiledPosterGrid: TiledPosterGrid

    private val movieListViewModel: MovieListViewModel by context.viewModels()

    private val WINDOW_MARGIN = 16.dp()
    private val GRID_MARGIN_TOP = (40 + 32).dp()
    private val GRID_MARGIN_BOTTOM = 8.dp()

    private val PULLDOWN_END_DISTANCE = (64 + 64).dp()

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

            titleCategoryToggle = makeKeyIcon(context).apply {
                resolveAttribute(R.attr.actionBarItemBackground)?.let(::setBackgroundResource)
                setOnClickListener { movieListViewModel.toggleTitleCategory() }
            }
            barAddons = listOf(titleCategoryToggle)
        }.also(::addView)

        // Setup Movie List
        swipeRefreshLayout = SwipeRefreshLayout(context).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            resolveAttribute(R.attr.colorPrimaryDark)?.let { setColorSchemeResources(it) }
            resolveAttribute(android.R.attr.colorBackground)?.let(::setProgressBackgroundColorSchemeResource)

            setProgressViewEndTarget(false, PULLDOWN_END_DISTANCE)
            setOnRefreshListener { movieListViewModel.reload() }

            setOnUpdateWithWindowInsetsListener { _, insets, _, _ ->
                setProgressViewEndTarget(false, PULLDOWN_END_DISTANCE + insets.systemWindowInsetTop)

                insets.consumeSystemWindowInsets()
            }

        }.also(::addView)

        tiledPosterGrid = TiledPosterGrid(context).apply {
            setPadding(
                8.dp(),
                GRID_MARGIN_TOP,
                8.dp(),
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

        movieListViewModel.isSpinnerShown.bind(context) {
            swipeRefreshLayout.isRefreshing = it
        }.disposeBy(disposeBag)

        movieListViewModel.titleCategory.bind(context) {
            val icon = when (it) {
                TitleCategory.MOVIE -> R.drawable.ic_film
                TitleCategory.TV_SHOW -> R.drawable.ic_tv
            }
            titleCategoryToggle.setImageResource(icon)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        disposeBag.dispose()
    }
}

class MainPosterScreenTransitionRoute : TransitionRoute {
    override fun transition(): Transition = TransitionSet().apply {
        ChangeBounds()
            .addTarget(R.id.searchBarView)
            .setInterpolator(DecelerateInterpolator())
            .also { addTransition(it) }

        CornerRadiusTransition()
            .addTarget(R.id.searchBarView)
            .setInterpolator(DecelerateInterpolator())
            .also { addTransition(it) }

        Slide()
            .addTarget(R.id.tilePosterView)
            .setInterpolator(DecelerateInterpolator()).also { addTransition(it) }
    }
}
