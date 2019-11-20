package com.kgbier.kgbmd.view.ui

import android.content.Context
import android.view.View
import android.widget.ImageView
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
    val textViewRating: TextView

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

        textViewTitle = TextView(context).apply {
            id = View.generateViewId()

            textSize = 16f
        }.also(::addView)

        ConstraintSet().apply {
            constrainWidth(textViewTitle.id, WRAP_CONTENT)
            constrainHeight(textViewTitle.id, WRAP_CONTENT)

            setMargin(textViewTitle.id, START, dp(8))
            connect(
                textViewTitle.id,
                START,
                imageViewThumbnail.id,
                END
            )
            connect(
                textViewTitle.id,
                TOP,
                imageViewThumbnail.id,
                TOP
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
                textViewTitle.id,
                START
            )
            connect(
                textViewTidbit.id,
                TOP,
                textViewTitle.id,
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

        textViewYear = TextView(context).apply {
            id = View.generateViewId()

            setLines(1)
            textSize = 12f
        }.also(::addView)


        textViewRating = TextView(context).apply {
            id = View.generateViewId()

            setLines(1)
            textSize = 16f
        }.also(::addView)

        ConstraintSet().apply {
            constrainWidth(textViewYear.id, 0)
            constrainHeight(textViewYear.id, WRAP_CONTENT)

            setMargin(textViewYear.id, START, dp(4))
            setMargin(textViewYear.id, END, dp(4))

            connect(
                textViewYear.id,
                START,
                textViewTitle.id,
                END
            )
            connect(
                textViewYear.id,
                BASELINE,
                textViewTitle.id,
                BASELINE
            )
            connect(
                textViewYear.id,
                END,
                textViewRating.id,
                START
            )
        }.applyTo(this)

        ConstraintSet().apply {
            constrainWidth(textViewRating.id, WRAP_CONTENT)
            constrainHeight(textViewRating.id, WRAP_CONTENT)

            setMargin(textViewRating.id, END, dp(4))

            connect(
                textViewRating.id,
                BASELINE,
                textViewTitle.id,
                BASELINE
            )
            connect(
                textViewRating.id,
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
        textViewRating.visibility = View.VISIBLE
        textViewRating.text = it
    } ?: run {
        textViewRating.visibility = View.GONE
    }
}