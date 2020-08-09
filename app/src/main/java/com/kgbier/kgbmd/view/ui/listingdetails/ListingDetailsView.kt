package com.kgbier.kgbmd.view.ui.listingdetails

import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import androidx.core.view.setPadding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kgbier.kgbmd.util.dp

open class ListingDetailsView(context: Context) : RecyclerView(context) {
    val listingAdapter = ListingAdapter()

    init {
        layoutManager = LinearLayoutManager(context)
        adapter = listingAdapter
        setPadding(16.dp)
        clipToPadding = false
        this.addItemDecoration(BetweenItemDecoration(12.dp))
    }
}

interface BaseListingViewModel
abstract class BaseListingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(viewModel: BaseListingViewModel)
}

class ListingAdapter : ListAdapter<BaseListingViewModel, BaseListingViewHolder>(DIFF_CALLBACK) {
    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<BaseListingViewModel> =
            object : DiffUtil.ItemCallback<BaseListingViewModel>() {
                override fun areItemsTheSame(
                    oldItem: BaseListingViewModel, newItem: BaseListingViewModel
                ): Boolean = oldItem == newItem // TODO: improve this

                override fun areContentsTheSame(
                    oldItem: BaseListingViewModel, newItem: BaseListingViewModel
                ): Boolean = oldItem.hashCode() == newItem.hashCode()
            }

        const val VIEW_TYPE_TITLE = 0
        const val VIEW_TYPE_TITLED_TEXT = 1
    }

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is TitleListingViewModel -> VIEW_TYPE_TITLE
        is TitledTextListingViewModel -> VIEW_TYPE_TITLED_TEXT
        else -> throw NotImplementedError()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseListingViewHolder =
        when (viewType) {
            VIEW_TYPE_TITLE -> TitleListingViewHolder(parent.context)
            VIEW_TYPE_TITLED_TEXT -> TitledTextListingViewHolder(parent.context)
            else -> throw NotImplementedError()
        }

    override fun onBindViewHolder(holder: BaseListingViewHolder, position: Int) =
        holder.bind(getItem(position))
}

class BetweenItemDecoration(private val spacing: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, itemPosition: Int, parent: RecyclerView) {
        if (itemPosition != 0) outRect.set(0, spacing, 0, 0)
    }
}
