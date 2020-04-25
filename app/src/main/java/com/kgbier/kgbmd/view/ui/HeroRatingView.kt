package com.kgbier.kgbmd.view.ui

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.ConstraintSet.*
import com.kgbier.kgbmd.R
import com.kgbier.kgbmd.util.dp
import com.kgbier.kgbmd.util.setTextColorAttr
import com.kgbier.kgbmd.util.setTextStyleAttr

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
            textViewRating.text = "/ 10"
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

        ConstraintSet().apply {
            constrainWidth(textViewRating.id, WRAP_CONTENT)
            constrainHeight(textViewRating.id, WRAP_CONTENT)

            connect(
                textViewRating.id,
                START,
                PARENT_ID,
                START
            )
            connect(
                textViewRating.id,
                TOP,
                PARENT_ID,
                TOP
            )
        }.applyTo(this)

        ConstraintSet().apply {
            constrainWidth(ratingStarView.id, WRAP_CONTENT)
            constrainHeight(ratingStarView.id, WRAP_CONTENT)
            setMargin(ratingStarView.id, START, 4.dp())

            connect(
                ratingStarView.id,
                START,
                textViewRating.id,
                END
            )
            connect(
                ratingStarView.id,
                BASELINE,
                textViewRating.id,
                BASELINE
            )
            connect(
                ratingStarView.id,
                END,
                PARENT_ID,
                END
            )
        }.applyTo(this)

        ConstraintSet().apply {
            constrainWidth(textViewRatingCount.id, WRAP_CONTENT)
            constrainHeight(textViewRatingCount.id, WRAP_CONTENT)

            connect(
                textViewRatingCount.id,
                START,
                PARENT_ID,
                START
            )
            connect(
                textViewRatingCount.id,
                BOTTOM,
                PARENT_ID,
                BOTTOM
            )
            connect(
                textViewRatingCount.id,
                TOP,
                textViewRating.id,
                BOTTOM
            )
            connect(
                textViewRatingCount.id,
                END,
                PARENT_ID,
                END
            )
        }.applyTo(this)
    }

    fun setRating(rating: String, count: String?) {
        textViewRating.text = rating
        textViewRatingCount.text = count
    }
}
