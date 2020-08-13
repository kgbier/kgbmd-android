package com.kgbier.kgbmd.view.ui

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.kgbier.kgbmd.R
import com.kgbier.kgbmd.util.setTextColorAttr
import com.kgbier.kgbmd.util.setTextStyleAttr

class CastMemberView(context: Context) : LinearLayout(context) {
    val textViewName: TextView
    val textViewRole: TextView

    init {
        orientation = VERTICAL

        textViewName = TextView(context).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )
            setTextStyleAttr(R.attr.textAppearanceBody1)
            setTextColorAttr(android.R.attr.textColorPrimary)
        }.also(::addView)

        textViewRole = TextView(context).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )
            setTextStyleAttr(R.attr.textAppearanceBody1)
            setTextColorAttr(android.R.attr.textColorSecondary)

            visibility = View.GONE
        }.also(::addView)
    }

    fun setText(name: String, role: String?) {
        textViewName.text = name
        if (role == null) {
            textViewRole.visibility = View.GONE
        } else {
            textViewRole.visibility = View.VISIBLE
            textViewRole.text = role
        }
    }
}
