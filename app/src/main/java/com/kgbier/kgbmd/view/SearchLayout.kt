package com.kgbier.kgbmd.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import androidx.core.view.updateLayoutParams
import androidx.core.view.updateMargins
import androidx.core.view.updateMarginsRelative
import com.kgbier.kgbmd.MainActivity
import com.kgbier.kgbmd.R
import com.kgbier.kgbmd.util.dp
import com.kgbier.kgbmd.util.resolveAttribute
import com.kgbier.kgbmd.util.setOnUpdateWithWindowInsetsListener
import com.kgbier.kgbmd.view.component.SearchBar
import com.kgbier.kgbmd.view.component.SearchResults

@SuppressLint("ViewConstructor")
class SearchLayout(context: MainActivity) : LinearLayout(context) {

    private val searchBar: SearchBar
    private val searchResults: SearchResults

    private val WINDOW_MARGIN = dp(16)

    init {
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
            layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(WINDOW_MARGIN, dp(8), WINDOW_MARGIN, WINDOW_MARGIN)
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
}