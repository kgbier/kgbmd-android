package com.kgbier.kgbmd.view.ui

import android.content.Context
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.kgbier.kgbmd.R
import com.kgbier.kgbmd.util.setTextColorAttr
import com.kgbier.kgbmd.util.setTextStyleAttr

class TitledTextView(context: Context) : LinearLayout(context) {
    val textViewTitle: TextView
    val textViewContent: TextView

    init {
        orientation = VERTICAL

        textViewTitle = TextView(context).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )
            setTextStyleAttr(R.attr.textAppearanceOverline)
            setTextColorAttr(android.R.attr.textColorSecondary)
        }.also(::addView)

        textViewContent = TextView(context).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )
            setTextStyleAttr(R.attr.textAppearanceBody2)
            setTextColorAttr(android.R.attr.textColorPrimary)
        }.also(::addView)
    }

    var text: CharSequence?
        get() = textViewContent.text
        set(value) {
            textViewContent.text = value
        }
}
