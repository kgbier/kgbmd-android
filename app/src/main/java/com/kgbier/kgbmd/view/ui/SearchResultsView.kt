package com.kgbier.kgbmd.view.ui

import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kgbier.kgbmd.R
import com.kgbier.kgbmd.domain.SearchSuggestion
import com.kgbier.kgbmd.util.dp

private const val ELEVATION = 4f

@SuppressLint("ViewConstructor")
open class SearchResultsView(context: Context) : CardView(context) {
    val resultAdapter = ResultAdapter()

    init {
        id = R.id.searchResultsView
        isTransitionGroup = true

        radius = dp(4f)
        cardElevation = dp(ELEVATION)

        RecyclerView(context).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)

            adapter = resultAdapter
            layoutManager = LinearLayoutManager(context)
        }.also(::addView)
    }
}

class ResultViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    TextView(parent.context).apply {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
    }
) {
    val view: TextView = itemView as TextView
}

class ResultAdapter : ListAdapter<SearchSuggestion, ResultViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<SearchSuggestion> =
            object : DiffUtil.ItemCallback<SearchSuggestion>() {
                override fun areItemsTheSame(
                    oldItem: SearchSuggestion, newItem: SearchSuggestion
                ): Boolean = oldItem.hashCode() == newItem.hashCode()

                override fun areContentsTheSame(
                    oldItem: SearchSuggestion, newItem: SearchSuggestion
                ): Boolean = oldItem.hashCode() == newItem.hashCode()
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder =
        ResultViewHolder(parent)

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        with(getItem(position)) {
            holder.view.text = title
        }
    }
}
