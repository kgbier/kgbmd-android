package com.kgbier.kgbmd.view.ui

import android.content.Context
import android.view.Gravity
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.updateLayoutParams
import com.kgbier.kgbmd.R
import com.kgbier.kgbmd.util.dp
import com.kgbier.kgbmd.util.setTextStyleAttr

class RatingStarView(context: Context) : LinearLayout(context) {

    val textViewRating: TextView
    val imageViewStar: ImageView

    init {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL

        textViewRating = TextView(context).apply {
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            setTextStyleAttr(com.google.android.material.R.attr.textAppearanceCaption)
            includeFontPadding = false
        }.also(::addView)

        imageViewStar = ImageView(context).apply {
            layoutParams = LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.MATCH_PARENT
            ).apply { marginStart = 2.dp }
            setImageResource(R.drawable.ic_star)
        }.also(::addView)
    }

    override fun getBaseline(): Int = textViewRating.baseline

    override fun onAttachedToWindow() {
        imageViewStar.updateLayoutParams {
            val targetSize = (textViewRating.textSize * 1.2f).toInt()
            width = targetSize
            height = targetSize
        }
        super.onAttachedToWindow()
    }

    private var isSpinning = false
    fun spin() {
        isSpinning = true
        imageViewStar.animate().rotationBy(720f).setInterpolator(AccelerateDecelerateInterpolator())
            .setDuration(2000).withEndAction { spin() }
    }

    fun cancelSpin() {
        isSpinning = false
        imageViewStar.animate().cancel()
        imageViewStar.rotation = 0f
    }
}
