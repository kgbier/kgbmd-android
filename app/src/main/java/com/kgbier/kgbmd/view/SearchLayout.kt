package com.kgbier.kgbmd.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import com.kgbier.kgbmd.MainActivity
import com.kgbier.kgbmd.util.dp
import com.kgbier.kgbmd.view.component.SearchBar
import com.kgbier.kgbmd.view.component.SearchResults

@SuppressLint("ViewConstructor")
class SearchLayout(context: MainActivity) : LinearLayout(context) {

    private val searchBar: SearchBar
    private val searchResults: SearchResults

    init {
        orientation = VERTICAL

        // Setup Search Bar
        searchBar = SearchBar(context).apply {
            layoutParams =
                LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(dp(16), dp(16), dp(16), 0)
                }
        }.also(::addView)

        if (searchBar.editTextSearch.requestFocus()) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        }

        searchResults = SearchResults(context).apply {
            layoutParams =
                LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(dp(16), dp(8), dp(16), dp(16))
                }
        }.also(::addView)
    }
}