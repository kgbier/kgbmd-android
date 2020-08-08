package com.kgbier.kgbmd.view.ui

import android.content.Context
import android.graphics.Rect
import android.view.ViewGroup
import androidx.core.view.setPadding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kgbier.kgbmd.R
import com.kgbier.kgbmd.domain.model.MoviePoster
import com.kgbier.kgbmd.util.dp

open class TiledPosterView(context: Context) : RecyclerView(context) {
    val posterAdapter = PosterAdapter()

    companion object {
        const val DESIRED_POSTER_WIDTH_DP = 120
    }

    private fun estimateColumns(): Int {
        with(resources.displayMetrics) {
            return ((widthPixels / density) / DESIRED_POSTER_WIDTH_DP).toInt()
        }
    }

    init {
        id = R.id.tilePosterView
        isTransitionGroup = true

        adapter = PosterLoadingAdapter()
        layoutManager = GridLayoutManager(context, estimateColumns())
        setPadding(8.dp)
        clipToPadding = false
        this.addItemDecoration(InsetItemDecoration(8.dp))
    }
}

class PosterViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    PosterView(parent.context)
) {
    val view: PosterView = itemView as PosterView
}

class PosterLoadingAdapter : RecyclerView.Adapter<PosterViewHolder>() {
    companion object {
        private const val LOADING_COUNT = 29
    }

    override fun getItemCount(): Int = LOADING_COUNT

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PosterViewHolder =
        PosterViewHolder(parent)

    override fun onBindViewHolder(holder: PosterViewHolder, position: Int) = Unit
}

class PosterAdapter : ListAdapter<MoviePoster, PosterViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<MoviePoster> =
            object : DiffUtil.ItemCallback<MoviePoster>() {
                override fun areItemsTheSame(
                    oldItem: MoviePoster, newItem: MoviePoster
                ): Boolean = oldItem.ttId == newItem.ttId

                override fun areContentsTheSame(
                    oldItem: MoviePoster, newItem: MoviePoster
                ): Boolean = oldItem.hashCode() == newItem.hashCode()
            }
    }

    var onItemClickListener: ((titleId: String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PosterViewHolder =
        PosterViewHolder(parent)

    override fun onBindViewHolder(holder: PosterViewHolder, position: Int) {
        with(getItem(position)) {
            holder.view.setTitle(title)
            holder.view.setRating(rating)
            holder.view.setPoster(thumbnailUrl, posterUrlSmall)
            holder.view.setOnClickListener { onItemClickListener?.invoke(ttId) }
        }
    }
}

class InsetItemDecoration(private val spacing: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, itemPosition: Int, parent: RecyclerView) {
        outRect.set(spacing, spacing, spacing, spacing)
    }
}
