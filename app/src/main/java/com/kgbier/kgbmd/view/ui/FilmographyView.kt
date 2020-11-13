package com.kgbier.kgbmd.view.ui

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.kgbier.kgbmd.R
import com.kgbier.kgbmd.util.dp
import com.kgbier.kgbmd.util.resolveAttribute
import com.kgbier.kgbmd.util.setTextColorAttr
import com.kgbier.kgbmd.util.setTextStyleAttr

class FilmographyView(context: Context) : LinearLayout(context) {
    val imageViewOpenInNew: ImageView

    val textViewYear: TextView
    val textViewTitle: TextView
    val textViewRole: TextView

    init {
        orientation = HORIZONTAL

        background = resolveAttribute(R.attr.selectableItemBackground)
            ?.let { ContextCompat.getDrawable(context, it) }

        LinearLayout(context).apply {
            layoutParams =
                LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1f).apply {
                    gravity = Gravity.CENTER_VERTICAL
                }
            orientation = VERTICAL

            textViewYear = TextView(context).apply {
                layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
                setTextStyleAttr(R.attr.textAppearanceCaption)
                setTextColorAttr(android.R.attr.textColorSecondary)

                visibility = View.GONE
            }.also(::addView)

            textViewTitle = TextView(context).apply {
                layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
                setTextStyleAttr(R.attr.textAppearanceBody1)
                setTextColorAttr(android.R.attr.textColorPrimary)
            }.also(::addView)

            textViewRole = TextView(context).apply {
                layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
                setTextStyleAttr(R.attr.textAppearanceBody1)
                setTextColorAttr(android.R.attr.textColorSecondary)

                visibility = View.GONE
            }.also(::addView)

        }.also(::addView)

        imageViewOpenInNew = ImageView(context).apply {
            layoutParams = LayoutParams(24.dp, 24.dp).apply {
                gravity = Gravity.CENTER_VERTICAL
                marginStart = 16.dp
            }
            visibility = View.GONE
            setImageResource(R.drawable.ic_chevron_right)
        }.also(::addView)
    }

    fun setText(title: String, year: String?, role: String?) {
        textViewTitle.text = title
        if (year == null) {
            textViewYear.visibility = View.GONE
        } else {
            textViewYear.visibility = View.VISIBLE
            textViewYear.text = year
        }
        if (role == null) {
            textViewRole.visibility = View.GONE
        } else {
            textViewRole.visibility = View.VISIBLE
            textViewRole.text = role
        }
    }

    fun showAction(isVisible: Boolean) = if (isVisible) {
        imageViewOpenInNew.visibility = View.VISIBLE
    } else {
        imageViewOpenInNew.visibility = View.GONE
    }
}
