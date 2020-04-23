package com.kgbier.kgbmd.view.component

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kgbier.kgbmd.MainActivity
import com.kgbier.kgbmd.Route
import com.kgbier.kgbmd.domain.model.Suggestion
import com.kgbier.kgbmd.util.LiveDataDisposeBag
import com.kgbier.kgbmd.util.bind
import com.kgbier.kgbmd.util.disposeBy
import com.kgbier.kgbmd.view.ui.SearchResultsView
import com.kgbier.kgbmd.view.ui.SearchSuggestionView
import com.kgbier.kgbmd.view.viewmodel.MovieListSearchViewModel
import com.kgbier.kgbmd.view.viewmodel.RatingResult

@SuppressLint("ViewConstructor")
class SearchResults(context: MainActivity) : SearchResultsView(context) {

    private val movieListSearchViewModel: MovieListSearchViewModel by context.viewModels()

    private val resultAdapter = ResultAdapter(context)

    private val disposeBag = LiveDataDisposeBag()

    init {
        visibility = View.GONE
        emptyStateMessage.visibility = View.GONE
        resultRecyclerView.adapter = resultAdapter

        movieListSearchViewModel.isFirstLoad.bind(context) {
            if (it) {
                visibility = View.VISIBLE
                emptyStateMessage.visibility = View.GONE
                loadingProgressBar.visibility = View.VISIBLE
            }
        }.disposeBy(disposeBag)

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
        }.disposeBy(disposeBag)

        resultAdapter.onItemClickListener = { position ->
            context.navigate(Route.DetailScreen("tt0076759"))
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        disposeBag.dispose()
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

    private val movieListSearchViewModel: MovieListSearchViewModel by context.viewModels()

    private var disposeBag = LiveDataDisposeBag()

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
            when (it) {
                RatingResult.Loading -> view.setRating(null, true)
                is RatingResult.Result -> view.setRating(it.rating, false)
            }

        }?.disposeBy(disposeBag) ?: view.setRating(null, false)
    }

    fun onViewRecycled() = disposeBag.dispose()
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

    var onItemClickListener: ((position: Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder =
        ResultViewHolder(parent, context)

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        holder.onBindData(getItem(position))
        holder.view.setOnClickListener { onItemClickListener?.invoke(position) }
    }

    override fun onViewRecycled(holder: ResultViewHolder) = holder.onViewRecycled()
}