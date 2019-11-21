package com.kgbier.kgbmd.view.ui

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import com.kgbier.kgbmd.R
import com.kgbier.kgbmd.util.dp
import com.kgbier.kgbmd.util.sp

class RatingStarView(context: Context) : LinearLayout(context) {

    val textViewRating: TextView
    val imageViewStar: ImageView

    init {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL

        textViewRating = TextView(context).apply {
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            setTextColor(Color.WHITE)
        }.also(::addView)

        imageViewStar = ImageView(context).apply {
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
            setImageResource(R.drawable.ic_star)
            updatePadding(top = dp(2))
        }.also(::addView)

        setTextSize(14f)
    }

    fun setTextSize(size: Float) {
        textViewRating.textSize = size
        imageViewStar.updateLayoutParams {
            width = sp(size * 1.2f).toInt()
            height = sp(size * 1.2f).toInt()
        }
    }
}