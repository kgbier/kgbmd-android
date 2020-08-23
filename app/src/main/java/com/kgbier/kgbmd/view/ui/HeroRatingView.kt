package com.kgbier.kgbmd.view.ui

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet.WRAP_CONTENT
import com.kgbier.kgbmd.R
import com.kgbier.kgbmd.util.*

open class HeroRatingView(context: Context) : ConstraintLayout(context) {

    val textViewRating: TextView
    val ratingStarView: RatingStarView
    val textViewRatingCount: TextView

    init {

        textViewRating = TextView(context).apply {
            id = View.generateViewId()
            layoutParams = LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
            )
            setTextStyleAttr(R.attr.textAppearanceHeadline4)
            setTextColorAttr(android.R.attr.textColorPrimary)
            setLines(1)
        }.also(::addView)

        ratingStarView = RatingStarView(context).apply {
            id = View.generateViewId()
            textViewRating.setTextStyleAttr(R.attr.textAppearanceSubtitle1)
            textViewRating.setTextColorAttr(android.R.attr.textColorSecondary)
        }.also(::addView)

        textViewRatingCount = TextView(context).apply {
            id = View.generateViewId()
            layoutParams = LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
            )
            setTextStyleAttr(R.attr.textAppearanceCaption)
            setTextColorAttr(android.R.attr.textColorSecondary)
            setLines(1)
        }.also(::addView)

        constraintSet {
            val textViewRatingRef = constrain(textViewRating) {
                link(start, parent.start)
                link(top, parent.top)

                width(WRAP_CONTENT)
                height(WRAP_CONTENT)
            }

            constrain(ratingStarView) {
                link(start, textViewRatingRef.end, margin = 4.dp)
                link(baseline, textViewRatingRef.baseline)
                link(end, parent.end)

                width(WRAP_CONTENT)
                height(WRAP_CONTENT)
            }

            constrain(textViewRatingCount) {
                link(start, parent.start)
                link(bottom, parent.bottom)
                link(top, textViewRatingRef.bottom)
                link(end, parent.end)

                width(WRAP_CONTENT)
                height(WRAP_CONTENT)
            }
        }
    }

    fun setRating(rating: String, bestRating: String?, count: String?) {
        textViewRating.text = rating
        ratingStarView.textViewRating.text = "/ ${bestRating ?: "10"}"
        count?.let {
            textViewRatingCount.visibility = View.VISIBLE
            textViewRatingCount.text = it
        } ?: run {
            textViewRatingCount.visibility = View.GONE
        }
    }
}
