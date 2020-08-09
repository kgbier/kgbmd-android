package com.kgbier.kgbmd.view.ui.listingdetails

import android.content.Context
import android.view.ViewGroup
import com.kgbier.kgbmd.view.component.ListingTitle

class TitleListingViewModel(val name: String, val yearReleased: String?) : BaseListingViewModel

class TitleListingViewHolder(context: Context) : BaseListingViewHolder(ListingTitle(context).apply {
    layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
}) {
    val view get() = itemView as ListingTitle

    override fun bind(viewModel: BaseListingViewModel) {
        if (viewModel !is TitleListingViewModel) return

        view.setTitleSequence(viewModel.name, viewModel.yearReleased)
    }
}
