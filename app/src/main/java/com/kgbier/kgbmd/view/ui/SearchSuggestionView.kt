package com.kgbier.kgbmd.view.ui

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.ConstraintSet.*
import com.bumptech.glide.Glide
import com.kgbier.kgbmd.util.dp

open class SearchSuggestionView(context: Context) : ConstraintLayout(context) {

    val imageViewThumbnail: ImageView
    val textViewTitle: TextView
    val textViewTidbit: TextView
    val textViewYear: TextView
    val ratingStarView: RatingStarView

    init {

        imageViewThumbnail = ImageView(context).apply {
            id = View.generateViewId()

            scaleType = ImageView.ScaleType.CENTER_CROP
        }.also(::addView)

        ConstraintSet().apply {
            constrainWidth(imageViewThumbnail.id, dp(32))
            constrainHeight(imageViewThumbnail.id, dp(48))

            setMargin(imageViewThumbnail.id, TOP, dp(2))
            setMargin(imageViewThumbnail.id, BOTTOM, dp(2))

            connect(
                imageViewThumbnail.id,
                START,
                PARENT_ID,
                START
            )
            connect(
                imageViewThumbnail.id,
                TOP,
                PARENT_ID,
                TOP
            )
            connect(
                imageViewThumbnail.id,
                BOTTOM,
                PARENT_ID,
                BOTTOM
            )
        }.applyTo(this)

        val layoutTitleText = LinearLayout(context).apply {
            id = View.generateViewId()

            layoutParams = LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
            )

            LinearLayout(context).apply {
                textViewTitle = TextView(context).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT,
                        1f
                    )
                    ellipsize = TextUtils.TruncateAt.END
                    isSingleLine = true
                    textSize = 16f
                }.also(::addView)

                textViewYear = TextView(context).apply {
                    layoutParams =
                        LinearLayout.LayoutParams(
                            LayoutParams.WRAP_CONTENT,
                            LayoutParams.WRAP_CONTENT
                        ).apply {
                            marginStart = dp(4)
                        }
                    setLines(1)
                    textSize = 12f
                }.also(::addView)
            }.also(::addView)
        }.also(::addView)

        ratingStarView = RatingStarView(context).apply {
            id = View.generateViewId()
            setTextSize(16f)
        }.also(::addView)

        ConstraintSet().apply {
            constrainWidth(ratingStarView.id, WRAP_CONTENT)
            constrainHeight(ratingStarView.id, WRAP_CONTENT)
            setVisibility(ratingStarView.id, ConstraintSet.GONE)

            setMargin(ratingStarView.id, END, dp(4))

            connect(
                ratingStarView.id,
                TOP,
                layoutTitleText.id,
                TOP
            )
            connect(
                ratingStarView.id,
                END,
                PARENT_ID,
                END
            )
        }.applyTo(this)

        ConstraintSet().apply {
            constrainWidth(layoutTitleText.id, 0)
            constrainHeight(layoutTitleText.id, WRAP_CONTENT)

            setMargin(layoutTitleText.id, START, dp(8))
            setMargin(layoutTitleText.id, END, dp(8))

            connect(
                layoutTitleText.id,
                TOP,
                imageViewThumbnail.id,
                TOP
            )
            connect(
                layoutTitleText.id,
                START,
                imageViewThumbnail.id,
                END
            )
            connect(
                layoutTitleText.id,
                END,
                ratingStarView.id,
                START
            )
        }.applyTo(this)

        textViewTidbit = TextView(context).apply {
            id = View.generateViewId()

            textSize = 14f
        }.also(::addView)

        ConstraintSet().apply {
            constrainWidth(textViewTidbit.id, 0)
            constrainHeight(textViewTidbit.id, WRAP_CONTENT)

            setMargin(textViewTidbit.id, BOTTOM, dp(2))
            setMargin(textViewTidbit.id, END, dp(4))

            connect(
                textViewTidbit.id,
                START,
                layoutTitleText.id,
                START
            )
            connect(
                textViewTidbit.id,
                TOP,
                layoutTitleText.id,
                BOTTOM
            )
            connect(
                textViewTidbit.id,
                BOTTOM,
                PARENT_ID,
                BOTTOM
            )
            connect(
                textViewTidbit.id,
                END,
                PARENT_ID,
                END
            )
        }.applyTo(this)
    }

    fun setThumbnail(url: String?) {
        Glide.with(this)
            .load(url)
            .into(imageViewThumbnail)
    }

    fun setTitle(title: String) {
        textViewTitle.text = title
    }

    fun setYear(year: String?) = year?.let {
        textViewYear.visibility = View.VISIBLE
        textViewYear.text = "($it)"
    } ?: run {
        textViewYear.visibility = View.GONE
    }

    fun setTidbit(tidbit: String?) = tidbit?.let {
        textViewTidbit.visibility = View.VISIBLE
        textViewTidbit.text = it
    } ?: run {
        textViewTidbit.visibility = View.GONE
    }

    fun setRating(rating: String?) = rating?.let {
        ratingStarView.visibility = View.VISIBLE
        ratingStarView.textViewRating.text = it
    } ?: run {
        ratingStarView.visibility = View.GONE
    }
}