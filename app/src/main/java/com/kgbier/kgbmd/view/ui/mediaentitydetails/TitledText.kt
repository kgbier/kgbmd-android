package com.kgbier.kgbmd.view.ui.mediaentitydetails

import android.content.Context
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import androidx.core.view.updateMarginsRelative
import com.kgbier.kgbmd.util.dp
import com.kgbier.kgbmd.view.ui.TitledTextView

class TitledTextViewModel(val title: String, val content: String) : BaseMediaEntityListItemViewModel

class TitledTextViewHolder(context: Context) :
    BaseMediaEntityListItemViewHolder(TitledTextView(context).apply {
        layoutParams = ViewGroup.MarginLayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        ).apply {
            updateMarginsRelative(start = 16.dp, end = 16.dp)
        }
    }) {
    val view get() = itemView as TitledTextView

    override fun bind(viewModel: BaseMediaEntityListItemViewModel) {
        if (viewModel !is TitledTextViewModel) return

        view.textViewTitle.text = viewModel.title
        view.textViewContent.text = viewModel.content
    }
}
