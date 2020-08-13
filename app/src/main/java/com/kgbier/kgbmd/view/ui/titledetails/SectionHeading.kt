package com.kgbier.kgbmd.view.ui.titledetails

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.updateMarginsRelative
import com.kgbier.kgbmd.R
import com.kgbier.kgbmd.util.dp
import com.kgbier.kgbmd.util.setTextColorAttr
import com.kgbier.kgbmd.util.setTextStyleAttr

class SectionHeadingViewModel(val title: String) : BaseTitlesViewModel

class SectionHeadingViewHolder(context: Context) :
    BaseTitlesViewHolder(AppCompatTextView(context).apply {
        layoutParams = ViewGroup.MarginLayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            updateMarginsRelative(start = 16.dp, end = 16.dp)
        }
        setTextStyleAttr(R.attr.textAppearanceHeadline6)
        setTextColorAttr(android.R.attr.textColorPrimary)
    }) {
    val view get() = itemView as TextView

    override fun bind(viewModel: BaseTitlesViewModel) {
        if (viewModel !is SectionHeadingViewModel) return

        view.text = viewModel.title
    }
}
