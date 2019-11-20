package com.kgbier.kgbmd.view.component

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kgbier.kgbmd.MainActivity
import com.kgbier.kgbmd.domain.model.Suggestion
import com.kgbier.kgbmd.util.bind
import com.kgbier.kgbmd.view.ui.SearchResultsView
import com.kgbier.kgbmd.view.ui.SearchSuggestionView
import com.kgbier.kgbmd.view.viewmodel.MovieListSearchViewModel

@SuppressLint("ViewConstructor")
class SearchResults(context: MainActivity) : SearchResultsView(context) {

    private var movieListSearchViewModel: MovieListSearchViewModel =
        ViewModelProviders.of(context).get(MovieListSearchViewModel::class.java)

    private val resultAdapter = ResultAdapter(context)

    init {
        visibility = View.GONE
        emptyStateMessage.visibility = View.GONE
        resultRecyclerView.adapter = resultAdapter

        movieListSearchViewModel.clearSearchState()

        movieListSearchViewModel.isFirstLoad.bind(context) {
            if (it) {
                visibility = View.VISIBLE
                emptyStateMessage.visibility = View.GONE
                loadingProgressBar.visibility = View.VISIBLE
            }
        }

        movieListSearchViewModel.resultList.bind(context) {

            if (it == null) {
                visibility = View.GONE
                emptyStateMessage.visibility = View.GONE
                loadingProgressBar.visibility = View.GONE
                resultAdapter.submitList(null)

                return@bind
            }

            resultAdapter.submitList(it)
            loadingProgressBar.visibility = View.GONE
            if (it.isEmpty()) {
                emptyStateMessage.visibility = View.VISIBLE
            } else {
                emptyStateMessage.visibility = View.GONE
            }
        }
    }
}

class ResultViewHolder(parent: ViewGroup, val context: MainActivity) : RecyclerView.ViewHolder(
    SearchSuggestionView(parent.context).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
) {

    val view: SearchSuggestionView = itemView as SearchSuggestionView

    private var movieListSearchViewModel: MovieListSearchViewModel =
        ViewModelProviders.of(context).get(MovieListSearchViewModel::class.java)

    fun onBindData(searchSuggestion: Suggestion) {
        with(searchSuggestion) {
            view.setThumbnail(thumbnailUrl)
            view.setTitle(title)
            view.setTidbit(tidbit)
            view.setYear(year)
        }
        movieListSearchViewModel.liveRatingResult(
            searchSuggestion.id, searchSuggestion.type
        )?.bind(context) {
            view.setRating(it)
        }
    }

}

class ResultAdapter(val context: MainActivity) :
    ListAdapter<Suggestion, ResultViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Suggestion> =
            object : DiffUtil.ItemCallback<Suggestion>() {
                override fun areItemsTheSame(
                    oldItem: Suggestion, newItem: Suggestion
                ): Boolean = oldItem.id == newItem.id

                override fun areContentsTheSame(
                    oldItem: Suggestion, newItem: Suggestion
                ): Boolean = oldItem.hashCode() == newItem.hashCode()
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder =
        ResultViewHolder(parent, context)

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        holder.onBindData(getItem(position))
    }
}