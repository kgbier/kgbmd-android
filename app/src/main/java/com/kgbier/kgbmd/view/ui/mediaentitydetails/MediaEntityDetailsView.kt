package com.kgbier.kgbmd.view.ui.mediaentitydetails

import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kgbier.kgbmd.util.dp

open class MediaEntityDetailsView(context: Context) : RecyclerView(context) {
    val detailsAdapter = MediaEntityDetailsAdapter()

    init {
        layoutManager = LinearLayoutManager(context)
        adapter = detailsAdapter
        this.addItemDecoration(BetweenItemDecoration(12.dp) {
            when (it) {
                is TitledTextViewHolder,
                is SectionHeadingViewHolder -> true
                else -> false
            }
        })

        @Suppress("LeakingThis")
        addOnScrollListener(object : OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                (findViewHolderForAdapterPosition(0) as? HeaderViewHolder)?.let {
                    it.view.imageViewBackground.translationY = computeVerticalScrollOffset() * 0.4f
                }
            }
        })
    }
}

interface BaseMediaEntityListItemViewModel
abstract class BaseMediaEntityListItemViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {
    abstract fun bind(viewModel: BaseMediaEntityListItemViewModel)
}

class MediaEntityDetailsAdapter :
    ListAdapter<BaseMediaEntityListItemViewModel, BaseMediaEntityListItemViewHolder>(DIFF_CALLBACK) {
    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<BaseMediaEntityListItemViewModel> =
            object : DiffUtil.ItemCallback<BaseMediaEntityListItemViewModel>() {
                override fun areItemsTheSame(
                    oldItem: BaseMediaEntityListItemViewModel,
                    newItem: BaseMediaEntityListItemViewModel
                ): Boolean = oldItem == newItem // TODO: improve this

                override fun areContentsTheSame(
                    oldItem: BaseMediaEntityListItemViewModel,
                    newItem: BaseMediaEntityListItemViewModel
                ): Boolean = oldItem.hashCode() == newItem.hashCode()
            }

        const val VIEW_TYPE_HEADER = 0
        const val VIEW_TYPE_SECTION_HEADER = 1
        const val VIEW_TYPE_TITLED_TEXT = 2
        const val VIEW_TYPE_CAST_MEMBER = 3
    }

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is HeaderViewModel -> VIEW_TYPE_HEADER
        is SectionHeadingViewModel -> VIEW_TYPE_SECTION_HEADER
        is TitledTextViewModel -> VIEW_TYPE_TITLED_TEXT
        is CastMemberViewModel -> VIEW_TYPE_CAST_MEMBER
        else -> throw NotImplementedError()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseMediaEntityListItemViewHolder =
        when (viewType) {
            VIEW_TYPE_HEADER -> HeaderViewHolder(parent.context)
            VIEW_TYPE_SECTION_HEADER -> SectionHeadingViewHolder(parent.context)
            VIEW_TYPE_TITLED_TEXT -> TitledTextViewHolder(parent.context)
            VIEW_TYPE_CAST_MEMBER -> CastMemberViewHolder(parent.context)
            else -> throw NotImplementedError()
        }

    override fun onBindViewHolder(holder: BaseMediaEntityListItemViewHolder, position: Int) =
        holder.bind(getItem(position))
}

class BetweenItemDecoration(
    private val spacing: Int,
    private val decorateBefore: (viewHolder: RecyclerView.ViewHolder) -> Boolean = { true },
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val shouldDecorate = parent.findContainingViewHolder(view)
            ?.let(decorateBefore)
            ?: false
        if (shouldDecorate) {
            outRect.set(0, spacing, 0, 0)
        }
    }
}
