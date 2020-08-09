package com.kgbier.kgbmd.view.ui.listingdetails

import android.annotation.SuppressLint
import android.content.Context
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.TextAppearanceSpan
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.toSpannable
import com.kgbier.kgbmd.R
import com.kgbier.kgbmd.util.resolveAttribute
import com.kgbier.kgbmd.util.setTextColorAttr
import com.kgbier.kgbmd.util.setTextStyleAttr

@SuppressLint("ViewConstructor")
class TitleView(context: Context) : AppCompatTextView(context) {
    init {
        setTextStyleAttr(R.attr.textAppearanceHeadline5)
        setTextColorAttr(android.R.attr.textColorPrimary)
    }
}

class TitleListingViewModel(val name: String, val yearReleased: String?) : BaseListingViewModel

class TitleListingViewHolder(context: Context) : BaseListingViewHolder(TitleView(context).apply {
    layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
}) {
    val view get() = itemView as TitleView

    override fun bind(viewModel: BaseListingViewModel) {
        if (viewModel !is TitleListingViewModel) return

        val titleSequence = SpannableStringBuilder().apply {
            append(viewModel.name)
            viewModel.yearReleased?.let { yearReleased ->
                val subtitleStyleSpan =
                    TextAppearanceSpan(
                        view.context,
                        R.style.TextAppearance_MaterialComponents_Subtitle1
                    )
                val secondaryColorSpan =
                    view.resolveAttribute(android.R.attr.textColorSecondary)?.let {
                        ResourcesCompat.getColor(view.resources, it, view.context.theme)
                    }?.let {
                        ForegroundColorSpan(it)
                    }

                append("\u00A0") // non-breaking space
                setSpan(subtitleStyleSpan, length, length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                secondaryColorSpan?.let {
                    setSpan(it, length, length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                }
                append("($yearReleased)")
            }
        }.toSpannable()

        view.text = titleSequence
    }
}
