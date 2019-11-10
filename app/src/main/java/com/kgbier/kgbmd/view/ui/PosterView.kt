package com.kgbier.kgbmd.view.ui

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.Space
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.ConstraintSet.*
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import com.kgbier.kgbmd.R
import com.kgbier.kgbmd.util.dp
import com.kgbier.kgbmd.util.resolveAttribute

class PosterView(context: Context) : CardView(context) {
    private val imageViewPoster: ImageView
    private val textViewTitle: TextView
    private val viewCornerScrim: View
    private val textViewRating: TextView

    init {
        layoutParams = MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        radius = dp(4f)
        resolveAttribute(R.attr.selectableItemBackground)?.let {
            foreground = ResourcesCompat.getDrawable(resources, it, context.theme)
            isClickable = true
        }

        ConstraintLayout(context).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

            imageViewPoster = ImageView(context).apply {
                id = generateViewId()
                scaleType = ImageView.ScaleType.CENTER_CROP
            }.also(::addView)

            ConstraintSet().apply {
                connect(imageViewPoster.id, START, PARENT_ID, START)
                connect(imageViewPoster.id, END, PARENT_ID, END)
                connect(imageViewPoster.id, TOP, PARENT_ID, TOP)
                connect(imageViewPoster.id, BOTTOM, PARENT_ID, BOTTOM)

                setDimensionRatio(imageViewPoster.id, "10:16")
            }.applyTo(this)

            val viewBottomScrim = View(context).apply {
                id = generateViewId()

                setBackgroundResource(R.drawable.scrim_overlay_bottom)
            }.also(::addView)

            textViewTitle = TextView(context).apply {
                id = generateViewId()

                setTextColor(Color.WHITE)
                textAlignment = TextView.TEXT_ALIGNMENT_VIEW_END
            }.also(::addView)

            val spaceTitleTop = Space(context).apply {
                id = generateViewId()
            }.also(::addView)

            viewCornerScrim = View(context).apply {
                id = generateViewId()

                setBackgroundResource(R.drawable.scrim_overlay_corner)
            }.also(::addView)

            textViewRating = TextView(context).apply {
                id = generateViewId()

                setCompoundDrawablesRelativeWithIntrinsicBounds(
                    null,
                    null,
                    ResourcesCompat.getDrawable(resources, R.drawable.rating_star, null),
                    null
                )
                setTextColor(Color.WHITE)
            }.also(::addView)

            val spaceRatingStart = Space(context).apply {
                id = generateViewId()
            }.also(::addView)

            ConstraintSet().apply {
                constrainHeight(textViewTitle.id, WRAP_CONTENT)

                setMargin(textViewTitle.id, START, dp(4))
                setMargin(textViewTitle.id, END, dp(4))
                setMargin(textViewTitle.id, BOTTOM, dp(4))

                connect(textViewTitle.id, START, PARENT_ID, START)
                connect(textViewTitle.id, END, PARENT_ID, END)
                connect(textViewTitle.id, BOTTOM, PARENT_ID, BOTTOM)

                constrainHeight(spaceTitleTop.id, dp(8))

                connect(spaceTitleTop.id, START, PARENT_ID, START)
                connect(spaceTitleTop.id, END, PARENT_ID, END)
                connect(spaceTitleTop.id, BOTTOM, textViewTitle.id, TOP)
            }.applyTo(this)

            ConstraintSet().apply {
                constrainWidth(textViewRating.id, WRAP_CONTENT)
                constrainHeight(textViewRating.id, WRAP_CONTENT)

                setMargin(textViewRating.id, TOP, dp(4))
                setMargin(textViewRating.id, END, dp(4))

                connect(textViewRating.id, END, PARENT_ID, END)
                connect(textViewRating.id, TOP, PARENT_ID, TOP)

                constrainWidth(spaceRatingStart.id, dp(24))

                connect(spaceRatingStart.id, END, textViewRating.id, START)
                connect(spaceRatingStart.id, TOP, PARENT_ID, TOP)
            }.applyTo(this)

            ConstraintSet().apply {
                connect(viewBottomScrim.id, START, PARENT_ID, START)
                connect(viewBottomScrim.id, END, PARENT_ID, END)
                connect(viewBottomScrim.id, TOP, spaceTitleTop.id, TOP)
                connect(viewBottomScrim.id, BOTTOM, PARENT_ID, BOTTOM)
            }.applyTo(this)

            ConstraintSet().apply {
                connect(viewCornerScrim.id, START, spaceRatingStart.id, START)
                connect(viewCornerScrim.id, END, PARENT_ID, END)
                connect(viewCornerScrim.id, TOP, PARENT_ID, TOP)

                setDimensionRatio(viewCornerScrim.id, "1:1")
            }.applyTo(this)

        }.also(::addView)
    }

    fun setRating(rating: String?) {
        rating?.let {
            viewCornerScrim.visibility = View.VISIBLE
            textViewRating.visibility = View.VISIBLE
            textViewRating.text = rating
        } ?: run {
            viewCornerScrim.visibility = View.GONE
            textViewRating.visibility = View.GONE
        }
    }

    fun setTitle(title: String) {
        textViewTitle.text = title
    }

    fun setPoster(thumbnailUrl: String, url: String) {
        Glide.with(this)
            .load(url)
            .thumbnail(
                Glide.with(this)
                    .load(thumbnailUrl)
            )
            .placeholder(R.drawable.film)
            .into(imageViewPoster)
    }
}