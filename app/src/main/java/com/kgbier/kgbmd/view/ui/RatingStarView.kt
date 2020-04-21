package com.kgbier.kgbmd.view.ui

import android.content.Context
import android.view.Gravity
import android.view.ViewTreeObserver
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.updateLayoutParams
import androidx.core.widget.TextViewCompat
import com.kgbier.kgbmd.R
import com.kgbier.kgbmd.util.dp

class RatingStarView(context: Context) : LinearLayout(context) {

    val textViewRating: TextView
    val imageViewStar: ImageView

    init {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL

        textViewRating = TextView(context).apply {
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            TextViewCompat.setTextAppearance(
                this,
                android.R.style.TextAppearance_Material_Caption
            )
            viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    viewTreeObserver.removeOnPreDrawListener(this)
                    processTextSize(textSize)
                    return true
                }
            })
            includeFontPadding = false
        }.also(::addView)

        imageViewStar = ImageView(context).apply {
            layoutParams = LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.MATCH_PARENT
            ).apply { marginStart = 2.dp() }
            setImageResource(R.drawable.ic_star)
        }.also(::addView)
    }

    private fun processTextSize(size: Float) {
        imageViewStar.updateLayoutParams {
            width = (size * 1.2f).toInt()
            height = (size * 1.2f).toInt()
        }
    }


    var isSpinning = false
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