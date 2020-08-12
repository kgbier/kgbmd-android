package com.kgbier.kgbmd.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.core.view.updateLayoutParams
import androidx.core.view.updateMargins
import androidx.core.view.updateMarginsRelative
import androidx.core.view.updatePadding
import androidx.transition.Scene
import androidx.transition.Slide
import androidx.transition.Transition
import androidx.transition.TransitionSet
import com.kgbier.kgbmd.*
import com.kgbier.kgbmd.util.*
import com.kgbier.kgbmd.view.component.SearchBar
import com.kgbier.kgbmd.view.component.SearchResults
import com.kgbier.kgbmd.view.viewmodel.MovieListSearchViewModel

@SuppressLint("ViewConstructor")
class SearchLayout(context: MainActivity) :
    RouteEventObserver,
    LinearLayout(context) {

    private val searchBar: SearchBar
    private val searchResults: SearchResults

    private val movieListSearchViewModel: MovieListSearchViewModel by context.viewModels()

    init {

        resolveColorAttribute(android.R.attr.colorBackground)?.let {
            setBackgroundColor(it)
        }

        orientation = VERTICAL

        searchBar = SearchBar(context).apply {
            layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                resolveDimensionAttribute(android.R.attr.actionBarSize) ?: LayoutParams.WRAP_CONTENT
            )

            radius = 0f

            imageViewKeyIcon.apply {
                resolveAttribute(R.attr.homeAsUpIndicator)?.let(::setImageResource)
                resolveAttribute(R.attr.actionBarItemBackground)?.let(::setBackgroundResource)
                setOnClickListener { context.navigateBack() }

                updateLayoutParams<LayoutParams> {
                    updateMarginsRelative(end = 12.dp)
                }
            }

            setOnUpdateWithWindowInsetsListener { _, insets, _, intendedMargin ->
                updateLayoutParams<LayoutParams> {
                    updateMargins(top = intendedMargin.top + insets.systemWindowInsetTop)
                }

                insets.consumeSystemWindowInsets()
            }
        }.also(::addView)

        searchResults = SearchResults(context).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, 0, 1f)
            clipToPadding = false

            setOnUpdateWithWindowInsetsListener { _, insets, _, _ ->
                updatePadding(bottom = insets.systemWindowInsetBottom)

                insets.consumeSystemWindowInsets()
            }
        }.also(::addView)
    }

    fun onViewShown() {
        if (searchBar.editTextSearch.requestFocus()) {
            with(context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager) {
                showSoftInput(searchBar.editTextSearch, InputMethodManager.SHOW_IMPLICIT)
            }
        }
    }

    override fun onExit() = movieListSearchViewModel.clearSearchState()
}

class SearchScreenSceneRoute : SceneRoute {
    override fun scene(rootView: ViewGroup, layout: View): Scene =
        Scene(rootView, layout).apply {
            if (layout is SearchLayout) {
                setEnterAction {
                    layout.onViewShown()
                }
            }
        }
}

class SearchScreenTransitionRoute : TransitionRoute {
    override fun transition(): Transition = TransitionSet().apply {
        Slide()
            .addTarget(R.id.searchResultsView)
            .setInterpolator(AccelerateInterpolator())
            .also { addTransition(it) }
    }
}
