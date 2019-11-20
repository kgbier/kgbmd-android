package com.kgbier.kgbmd.view.ui

import android.animation.LayoutTransition
import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.setMargins
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kgbier.kgbmd.R
import com.kgbier.kgbmd.util.dp
import com.kgbier.kgbmd.view.decoration.MiddleDividerItemDecoration

private const val ELEVATION = 4f

@SuppressLint("ViewConstructor")
open class SearchResultsView(context: Context) : CardView(context) {
    val resultRecyclerView: RecyclerView
    val loadingProgressBar: ProgressBar
    val emptyStateMessage: TextView

    init {
        id = R.id.searchResultsView
        isTransitionGroup = true

        layoutTransition = LayoutTransition()
        radius = dp(4f)
        cardElevation = dp(ELEVATION)

        loadingProgressBar = ProgressBar(context).apply {
            layoutParams = LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT,
                Gravity.CENTER
            ).apply {
                setMargins(dp(16))
            }
        }.also(::addView)

        emptyStateMessage = TextView(context).apply {
            layoutParams = LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT,
                Gravity.CENTER
            ).apply {
                setMargins(dp(16))
                text = "No results"
            }
        }.also(::addView)

        resultRecyclerView = RecyclerView(context).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            setPaddingRelative(dp(4), dp(2), dp(4), dp(2))
            clipToPadding = false
            itemAnimator = null
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(MiddleDividerItemDecoration(context))
        }.also(::addView)
    }
}
