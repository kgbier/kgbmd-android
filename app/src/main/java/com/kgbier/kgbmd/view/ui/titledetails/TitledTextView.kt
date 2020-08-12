package com.kgbier.kgbmd.view.ui.titledetails

import android.content.Context
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import androidx.core.view.updateMarginsRelative
import com.kgbier.kgbmd.util.dp
import com.kgbier.kgbmd.view.ui.TitledTextView

class TitledTextTitlesViewModel(val title: String, val content: String) : BaseTitlesViewModel

class TitledTextTitlesViewHolder(context: Context) :
    BaseTitlesViewHolder(TitledTextView(context).apply {
        layoutParams = ViewGroup.MarginLayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        ).apply {
            updateMarginsRelative(start = 16.dp, end = 16.dp)
        }
    }) {
    val view get() = itemView as TitledTextView

    override fun bind(viewModel: BaseTitlesViewModel) {
        if (viewModel !is TitledTextTitlesViewModel) return

        view.textViewTitle.text = viewModel.title
        view.textViewContent.text = viewModel.content
    }
}