package com.kgbier.kgbmd.view

import android.widget.LinearLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.kgbier.kgbmd.MainActivity
import com.kgbier.kgbmd.util.dp
import com.kgbier.kgbmd.view.component.SearchBar

class SearchLayout(context: MainActivity) : LinearLayout(context) {

    private val searchBar: SearchBar

    init {
        // Setup Search Bar
        searchBar = SearchBar(context).apply {
            layoutParams =
                CoordinatorLayout.LayoutParams(
                    CoordinatorLayout.LayoutParams.MATCH_PARENT,
                    CoordinatorLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    marginStart = dp(16)
                    marginEnd = dp(16)
                    topMargin = dp(16)
                }
        }.also(::addView)

        searchBar.editTextSearch.requestFocus()
    }
}