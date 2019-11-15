package com.kgbier.kgbmd.view.ui

import android.animation.LayoutTransition
import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.setMargins
import androidx.recyclerview.widget.*
import com.kgbier.kgbmd.R
import com.kgbier.kgbmd.domain.SearchSuggestion
import com.kgbier.kgbmd.util.dp

private const val ELEVATION = 4f

@SuppressLint("ViewConstructor")
open class SearchResultsView(context: Context) : CardView(context) {
    val resultAdapter = ResultAdapter()
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

        RecyclerView(context).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            setPaddingRelative(dp(12), dp(8), dp(12), dp(8))
            adapter = resultAdapter
            itemAnimator = null
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }.also(::addView)
    }
}

class ResultViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    SearchSuggestionView(parent.context).apply {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
    }
) {
    val view: SearchSuggestionView = itemView as SearchSuggestionView
}

class ResultAdapter : ListAdapter<SearchSuggestion, ResultViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<SearchSuggestion> =
            object : DiffUtil.ItemCallback<SearchSuggestion>() {
                override fun areItemsTheSame(
                    oldItem: SearchSuggestion, newItem: SearchSuggestion
                ): Boolean = oldItem.ttid == newItem.ttid

                override fun areContentsTheSame(
                    oldItem: SearchSuggestion, newItem: SearchSuggestion
                ): Boolean = oldItem.hashCode() == newItem.hashCode()
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder =
        ResultViewHolder(parent)

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        with(getItem(position)) {
            holder.view.setTitle(title)
            holder.view.setTidbit(tidbit)
            holder.view.setYear(year)
        }
    }
}
