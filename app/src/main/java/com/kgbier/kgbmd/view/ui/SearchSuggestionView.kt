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
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import com.kgbier.kgbmd.util.dp
import com.kgbier.kgbmd.util.resolveAttribute

open class SearchSuggestionView(context: Context) : ConstraintLayout(context) {

    val imageViewThumbnail: ImageView
    val textViewTitle: TextView
    val textViewTidbit: TextView
    val textViewYear: TextView
    val ratingStarView: RatingStarView

    init {

        setPadding(0, dp(8), 0, dp(8))

        imageViewThumbnail = ImageView(context).apply {
            id = View.generateViewId()

            scaleType = ImageView.ScaleType.CENTER_CROP
        }.also(::addView)

        ConstraintSet().apply {
            constrainWidth(imageViewThumbnail.id, dp(40))
            constrainHeight(imageViewThumbnail.id, dp(58))

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
                    resolveAttribute(android.R.attr.textColorPrimary)?.let {
                        setTextColor(ResourcesCompat.getColor(resources, it, context.theme))
                    }
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
                    resolveAttribute(android.R.attr.textColorTertiary)?.let {
                        setTextColor(ResourcesCompat.getColor(resources, it, context.theme))
                    }
                    setLines(1)
                    textSize = 12f
                }.also(::addView)
            }.also(::addView)
        }.also(::addView)

        ratingStarView = RatingStarView(context).apply {
            id = View.generateViewId()
            resolveAttribute(android.R.attr.textColorPrimary)?.let {
                textViewRating.setTextColor(ResourcesCompat.getColor(resources, it, context.theme))
            }
            setTextSize(16f)
        }.also(::addView)

        ConstraintSet().apply {
            constrainWidth(ratingStarView.id, WRAP_CONTENT)
            constrainHeight(ratingStarView.id, WRAP_CONTENT)
            setVisibility(ratingStarView.id, ConstraintSet.GONE)

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

        textViewTidbit = TextView(context).apply {
            id = View.generateViewId()
            resolveAttribute(android.R.attr.textColorSecondary)?.let {
                setTextColor(ResourcesCompat.getColor(resources, it, context.theme))
            }
            textSize = 14f
        }.also(::addView)

        ConstraintSet().apply {
            createVerticalChain(
                PARENT_ID,
                TOP,
                PARENT_ID,
                BOTTOM,
                arrayOf(layoutTitleText.id, textViewTidbit.id).toIntArray(),
                null,
                CHAIN_PACKED
            )
            setVerticalBias(layoutTitleText.id, 0f)

            // layoutTitleText
            constrainWidth(layoutTitleText.id, 0)
            constrainHeight(layoutTitleText.id, WRAP_CONTENT)

            setMargin(layoutTitleText.id, START, dp(16))
            setMargin(layoutTitleText.id, END, dp(8))
            setGoneMargin(layoutTitleText.id, START, dp(56))
            setGoneMargin(layoutTitleText.id, END, 0)

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

            // textViewTidbit
            constrainWidth(textViewTidbit.id, 0)
            constrainHeight(textViewTidbit.id, WRAP_CONTENT)

            connect(
                textViewTidbit.id,
                START,
                layoutTitleText.id,
                START
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
        imageViewThumbnail.visibility = url?.let { View.VISIBLE } ?: View.GONE

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

    fun setRating(rating: String?, loading: Boolean) {
        if (loading) {
            ratingStarView.visibility = View.VISIBLE
            ratingStarView.spin()
            ratingStarView.textViewRating.text = null
        } else {
            ratingStarView.cancelSpin()
            rating?.let {
                ratingStarView.visibility = View.VISIBLE
                ratingStarView.textViewRating.text = it
            } ?: run {
                ratingStarView.visibility = View.GONE
            }
        }
    }
}