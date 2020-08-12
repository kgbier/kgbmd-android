package com.kgbier.kgbmd.view.ui.titledetails

import android.content.Context
import android.view.ViewGroup
import com.kgbier.kgbmd.view.component.TitleHeading

class TitleHeadingViewModel(val name: String, val yearReleased: String?) : BaseTitlesViewModel

class TitleHeadingViewHolder(context: Context) : BaseTitlesViewHolder(TitleHeading(context).apply {
    layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
}) {
    val view get() = itemView as TitleHeading

    override fun bind(viewModel: BaseTitlesViewModel) {
        if (viewModel !is TitleHeadingViewModel) return

        view.setTitleSequence(viewModel.name, viewModel.yearReleased)
    }
}
