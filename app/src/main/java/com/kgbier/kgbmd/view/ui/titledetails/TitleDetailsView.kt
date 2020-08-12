package com.kgbier.kgbmd.view.ui.titledetails

import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kgbier.kgbmd.util.dp

open class TitleDetailsView(context: Context) : RecyclerView(context) {
    val titlesAdapter = TitlesAdapter()

    init {
        layoutManager = LinearLayoutManager(context)
        adapter = titlesAdapter
        this.addItemDecoration(BetweenItemDecoration(12.dp))
    }
}

interface BaseTitlesViewModel
abstract class BaseTitlesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(viewModel: BaseTitlesViewModel)
}

class TitlesAdapter : ListAdapter<BaseTitlesViewModel, BaseTitlesViewHolder>(DIFF_CALLBACK) {
    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<BaseTitlesViewModel> =
            object : DiffUtil.ItemCallback<BaseTitlesViewModel>() {
                override fun areItemsTheSame(
                    oldItem: BaseTitlesViewModel, newItem: BaseTitlesViewModel
                ): Boolean = oldItem == newItem // TODO: improve this

                override fun areContentsTheSame(
                    oldItem: BaseTitlesViewModel, newItem: BaseTitlesViewModel
                ): Boolean = oldItem.hashCode() == newItem.hashCode()
            }

        const val VIEW_TYPE_TITLE = 0
        const val VIEW_TYPE_TITLED_TEXT = 1
        const val VIEW_TYPE_HEADER = 2
    }

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is TitleHeadingViewModel -> VIEW_TYPE_TITLE
        is TitledTextTitlesViewModel -> VIEW_TYPE_TITLED_TEXT
        is HeaderViewModel -> VIEW_TYPE_HEADER
        else -> throw NotImplementedError()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseTitlesViewHolder =
        when (viewType) {
            VIEW_TYPE_TITLE -> TitleHeadingViewHolder(parent.context)
            VIEW_TYPE_TITLED_TEXT -> TitledTextTitlesViewHolder(parent.context)
            VIEW_TYPE_HEADER -> HeaderViewHolder(parent.context)
            else -> throw NotImplementedError()
        }

    override fun onBindViewHolder(holder: BaseTitlesViewHolder, position: Int) =
        holder.bind(getItem(position))
}

class BetweenItemDecoration(private val spacing: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, itemPosition: Int, parent: RecyclerView) {
        if (itemPosition != 0) outRect.set(0, spacing, 0, 0)
    }
}
