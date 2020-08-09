package com.kgbier.kgbmd.view.ui.listingdetails

import android.content.Context
import android.view.ViewGroup.LayoutParams
import com.kgbier.kgbmd.view.ui.TitledTextView

class TitledTextListingViewModel(val title: String, val content: String) : BaseListingViewModel

class TitledTextListingViewHolder(context: Context) :
    BaseListingViewHolder(TitledTextView(context).apply {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
    }) {
    val view get() = itemView as TitledTextView

    override fun bind(viewModel: BaseListingViewModel) {
        if (viewModel !is TitledTextListingViewModel) return

        view.textViewTitle.text = viewModel.title
        view.textViewContent.text = viewModel.content
    }
}
