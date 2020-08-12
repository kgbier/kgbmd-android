package com.kgbier.kgbmd.view.component

import android.annotation.SuppressLint
import android.content.Context
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.TextAppearanceSpan
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.toSpannable
import com.kgbier.kgbmd.R
import com.kgbier.kgbmd.util.resolveAttribute
import com.kgbier.kgbmd.util.setTextColorAttr
import com.kgbier.kgbmd.util.setTextStyleAttr

@SuppressLint("ViewConstructor")
class TitleHeading(context: Context) : AppCompatTextView(context) {
    init {
        setTextStyleAttr(R.attr.textAppearanceHeadline5)
        setTextColorAttr(android.R.attr.textColorPrimary)
    }

    fun setTitleSequence(name: String, subscript: String?) {
        text = SpannableStringBuilder().apply {
            append(name)
            subscript?.let { _ ->
                val subtitleStyleSpan = TextAppearanceSpan(
                    context,
                    R.style.TextAppearance_MaterialComponents_Subtitle1
                )
                val secondaryColorSpan =
                    resolveAttribute(android.R.attr.textColorSecondary)?.let {
                        ResourcesCompat.getColor(resources, it, context.theme)
                    }?.let {
                        ForegroundColorSpan(it)
                    }

                append("\u00A0") // non-breaking space
                setSpan(subtitleStyleSpan, length, length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                secondaryColorSpan?.let {
                    setSpan(it, length, length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                }
                append("($subscript)")
            }
        }.toSpannable()
    }
}
