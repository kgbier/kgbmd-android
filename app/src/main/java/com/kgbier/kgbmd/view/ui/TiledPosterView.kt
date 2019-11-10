package com.kgbier.kgbmd.view.ui

import android.content.Context
import android.graphics.Rect
import android.view.ViewGroup
import androidx.core.view.setPadding
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kgbier.kgbmd.data.parse.HotListItem
import com.kgbier.kgbmd.util.dp

private const val COLUMNS = 3

class TiledPosterView(context: Context) : RecyclerView(context) {
    val viewAdapter = TiledPosterAdapter()

    init {
        adapter = viewAdapter
        layoutManager = GridLayoutManager(context, COLUMNS)
        setPadding(dp(8))
        clipToPadding = false
        addItemDecoration(InsetItemDecoration(dp(8)))
    }

    fun setMovies(hotMovies: List<HotListItem>) {
        viewAdapter.setData(hotMovies)
    }
}

class TiledPosterViewHolder(val view: PosterView) : RecyclerView.ViewHolder(view)

class TiledPosterAdapter : RecyclerView.Adapter<TiledPosterViewHolder>() {
    private var movies: List<HotListItem> = emptyList()

    fun setData(list: List<HotListItem>) {
        movies = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = movies.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TiledPosterViewHolder {
        return TiledPosterViewHolder(
            PosterView(
                parent.context
            ).apply {
                layoutParams
            })
    }

    override fun onBindViewHolder(holder: TiledPosterViewHolder, position: Int) {
        val movie = movies[position]
        holder.view.setTitle(movie.name)
        holder.view.setRating(movie.rating)
        holder.view.setPoster(movie.thumbnailUrl, movie.posterUrl)
    }
}

class InsetItemDecoration(private val spacing: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, itemPosition: Int, parent: RecyclerView) {
        outRect.set(spacing, spacing, spacing, spacing)
    }
}
