package com.kgbier.kgbmd.view.component

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.toSpannable
import com.kgbier.kgbmd.R
import com.kgbier.kgbmd.util.*

@SuppressLint("ViewConstructor")
class TitleHeading(context: Context) : AppCompatTextView(context) {

    init {
        setTextStyleAttr(com.google.android.material.R.attr.textAppearanceHeadline5)
        setTextColorAttr(android.R.attr.textColorPrimary)
    }

    fun setTitleSequence(name: String, subscript: String?) {
        text = spannableStringBuilder(context) {
            append(name)
            subscript?.let { _ ->
                append("\u00A0") // non-breaking space

                withTextAppearance(
                    style = com.google.android.material.R.style.TextAppearance_MaterialComponents_Subtitle1,
                    colour = resolveAttribute(android.R.attr.textColorSecondary)
                ) {
                    append("($subscript)")
                }
            }
        }.toSpannable()
    }
}
