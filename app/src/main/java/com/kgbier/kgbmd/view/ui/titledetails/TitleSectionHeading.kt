package com.kgbier.kgbmd.view.ui.titledetails

import android.content.Context
import android.view.ViewGroup
import androidx.core.view.updateMarginsRelative
import com.kgbier.kgbmd.util.dp
import com.kgbier.kgbmd.view.component.TitleHeading

class TitleSectionHeadingViewModel(val name: String, val yearReleased: String?) :
    BaseTitlesViewModel

class TitleSectionHeadingViewHolder(context: Context) :
    BaseTitlesViewHolder(TitleHeading(context).apply {
        layoutParams = ViewGroup.MarginLayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            updateMarginsRelative(start = 16.dp, end = 16.dp)
        }
    }) {
    val view get() = itemView as TitleHeading

    override fun bind(viewModel: BaseTitlesViewModel) {
        if (viewModel !is TitleSectionHeadingViewModel) return

        view.setTitleSequence(viewModel.name, viewModel.yearReleased)
    }
}
