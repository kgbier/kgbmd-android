package com.kgbier.kgbmd.view.ui

import android.content.Context
import android.graphics.Rect
import android.view.ViewGroup
import androidx.core.view.setPadding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kgbier.kgbmd.data.parse.HotListItem
import com.kgbier.kgbmd.util.dp

class TiledPosterView(context: Context) : RecyclerView(context) {
    val posterAdapter = TiledPosterAdapter()

    companion object {
        const val DESIRED_POSTER_WIDTH_DP = 120
    }

    fun estimateColumns(): Int {
        with(resources.displayMetrics) {
            return ((widthPixels / density) / DESIRED_POSTER_WIDTH_DP).toInt()
        }
    }

    init {
        adapter = TiledPosterLoadingAdapter()
        layoutManager = GridLayoutManager(context, estimateColumns())
        setPadding(dp(8))
        clipToPadding = false
        addItemDecoration(InsetItemDecoration(dp(8)))
    }

    fun setMovies(hotMovies: List<HotListItem>) {
        posterAdapter.submitList(hotMovies)
        if (adapter is TiledPosterLoadingAdapter) {
            swapAdapter(posterAdapter, true)
        }
    }
}

class TiledPosterViewHolder(val view: PosterView) : RecyclerView.ViewHolder(view)

class TiledPosterLoadingAdapter : RecyclerView.Adapter<TiledPosterViewHolder>() {
    companion object {
        private const val LOADING_COUNT = 29
    }

    override fun getItemCount(): Int = LOADING_COUNT

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TiledPosterViewHolder =
        TiledPosterViewHolder(
            PosterView(
                parent.context
            )
        )

    override fun onBindViewHolder(holder: TiledPosterViewHolder, position: Int) = Unit
}

class TiledPosterAdapter : ListAdapter<HotListItem, TiledPosterViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<HotListItem> =
            object : DiffUtil.ItemCallback<HotListItem>() {
                override fun areItemsTheSame(
                    oldItem: HotListItem, newItem: HotListItem
                ): Boolean = oldItem.hashCode() == newItem.hashCode()

                override fun areContentsTheSame(
                    oldItem: HotListItem, newItem: HotListItem
                ): Boolean = oldItem.hashCode() == newItem.hashCode()
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TiledPosterViewHolder =
        TiledPosterViewHolder(
            PosterView(
                parent.context
            )
        )

    override fun onBindViewHolder(holder: TiledPosterViewHolder, position: Int) {
        with(getItem(position)) {
            holder.view.setTitle(name)
            holder.view.setRating(rating)
            holder.view.setPoster(thumbnailUrl, posterUrl)
        }
    }
}

class InsetItemDecoration(private val spacing: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, itemPosition: Int, parent: RecyclerView) {
        outRect.set(spacing, spacing, spacing, spacing)
    }
}