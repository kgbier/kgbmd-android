package com.kgbier.kgbmd.view.ui

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.setMargins
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kgbier.kgbmd.R
import com.kgbier.kgbmd.util.dp
import com.kgbier.kgbmd.view.decoration.MiddleDividerItemDecoration

private const val ELEVATION = 4f

@SuppressLint("ViewConstructor")
open class SearchResultsView(context: Context) : FrameLayout(context) {
    val resultRecyclerView: RecyclerView
    val loadingProgressBar: ProgressBar
    val emptyStateMessage: TextView

    init {
        id = R.id.searchResultsView
        isTransitionGroup = true

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
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            setPaddingRelative(dp(4), dp(2), dp(4), dp(2))
            clipToPadding = false
            itemAnimator = null
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(MiddleDividerItemDecoration(context))
        }.also(::addView)
    }

    override fun setPaddingRelative(start: Int, top: Int, end: Int, bottom: Int) {
        resultRecyclerView.setPaddingRelative(start, top, end, bottom)
    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        resultRecyclerView.setPadding(left, top, right, bottom)
    }
}
