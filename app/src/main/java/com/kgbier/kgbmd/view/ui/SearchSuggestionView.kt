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
import androidx.core.widget.TextViewCompat
import com.bumptech.glide.Glide
import com.kgbier.kgbmd.R
import com.kgbier.kgbmd.util.dp
import com.kgbier.kgbmd.util.resolveAttribute

open class SearchSuggestionView(context: Context) : ConstraintLayout(context) {

    val imageViewThumbnail: ImageView
    val textViewTitle: TextView
    val textViewTidbit: TextView
    val textViewYear: TextView
    val ratingStarView: RatingStarView

    init {

        setPadding(16.dp(), 8.dp(), 16.dp(), 8.dp())

        resolveAttribute(R.attr.selectableItemBackground)?.let {
            background = ResourcesCompat.getDrawable(resources, it, context.theme)
            isClickable = true
        }

        imageViewThumbnail = ImageView(context).apply {
            id = View.generateViewId()

            scaleType = ImageView.ScaleType.CENTER_CROP
        }.also(::addView)

        ConstraintSet().apply {
            constrainWidth(imageViewThumbnail.id, 40.dp())
            constrainHeight(imageViewThumbnail.id, 58.dp())

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
                    TextViewCompat.setTextAppearance(
                        this,
                        android.R.style.TextAppearance_Material_Body2
                    )
                    ellipsize = TextUtils.TruncateAt.END
                    isSingleLine = true
                }.also(::addView)

                textViewYear = TextView(context).apply {
                    layoutParams =
                        LinearLayout.LayoutParams(
                            LayoutParams.WRAP_CONTENT,
                            LayoutParams.WRAP_CONTENT
                        ).apply {
                            marginStart = 4.dp()
                        }
                    TextViewCompat.setTextAppearance(
                        this,
                        android.R.style.TextAppearance_Material_Caption
                    )
                    setLines(1)
                }.also(::addView)
            }.also(::addView)
        }.also(::addView)

        ratingStarView = RatingStarView(context).apply {
            id = View.generateViewId()
            TextViewCompat.setTextAppearance(
                textViewRating,
                android.R.style.TextAppearance_Material_Body1
            )
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
            TextViewCompat.setTextAppearance(
                this,
                android.R.style.TextAppearance_Material_Body1
            )
            resolveAttribute(android.R.attr.textColorSecondary)?.let {
                setTextColor(ResourcesCompat.getColor(resources, it, context.theme))
            }
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

            setMargin(layoutTitleText.id, START, 16.dp())
            setMargin(layoutTitleText.id, END, 8.dp())
            setGoneMargin(layoutTitleText.id, START, 56.dp())
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