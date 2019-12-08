package com.kgbier.kgbmd.view

import android.annotation.SuppressLint
import android.content.Context
import android.transition.*
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import androidx.core.view.updateLayoutParams
import androidx.core.view.updateMargins
import androidx.core.view.updateMarginsRelative
import com.kgbier.kgbmd.MainActivity
import com.kgbier.kgbmd.R
import com.kgbier.kgbmd.SceneRoute
import com.kgbier.kgbmd.TransitionRoute
import com.kgbier.kgbmd.util.dp
import com.kgbier.kgbmd.util.resolveAttribute
import com.kgbier.kgbmd.util.setOnUpdateWithWindowInsetsListener
import com.kgbier.kgbmd.view.animation.CornerRadiusTransition
import com.kgbier.kgbmd.view.component.SearchBar
import com.kgbier.kgbmd.view.component.SearchResults

@SuppressLint("ViewConstructor")
class SearchLayout(context: MainActivity) : LinearLayout(context) {

    private val searchBar: SearchBar
    private val searchResults: SearchResults

    private val WINDOW_MARGIN = dp(16)

    init {
        // Needs to be set when the app is laid out fullscreen. Otherwise when the keyboard is visible
        // the window does not get padded from the bottom to compensate for the keyboard.
        fitsSystemWindows = true

        orientation = VERTICAL

        // Setup Search Bar
        searchBar = SearchBar(context).apply {
            layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            )

            radius = 0f

            imageViewKeyIcon.apply {
                resolveAttribute(R.attr.homeAsUpIndicator)?.let(::setImageResource)
                resolveAttribute(R.attr.actionBarItemBackground)?.let(::setBackgroundResource)
                setOnClickListener { context.navigateBack() }

                updateLayoutParams<LayoutParams> {
                    updateMarginsRelative(end = dp(12))
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
            setPaddingRelative(WINDOW_MARGIN, 0, WINDOW_MARGIN, 0)
        }.also(::addView)
    }

    fun onViewShown() {
        if (searchBar.editTextSearch.requestFocus()) {
            with(context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager) {
                showSoftInput(searchBar.editTextSearch, InputMethodManager.SHOW_IMPLICIT)
            }
        }
    }
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
            .setInterpolator(AccelerateInterpolator()).also { addTransition(it) }
    }
}
