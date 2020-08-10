package com.kgbier.kgbmd.view.ui

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.Space
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet.WRAP_CONTENT
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import com.kgbier.kgbmd.R
import com.kgbier.kgbmd.util.*
import com.kgbier.kgbmd.view.animation.ElevateCardViewStateListAnimator
import com.kgbier.kgbmd.view.drawable.ShimmerDrawable

class PosterView(context: Context) : CardView(context) {
    private val imageViewPoster: ImageView
    private val viewBottomScrim: View
    private val textViewTitle: TextView
    private val viewCornerScrim: View
    private val ratingStarView: RatingStarView

    private val shimmer = ShimmerDrawable()

    init {
        layoutParams = MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        radius = 4f.dp
        resolveAttribute(R.attr.selectableItemBackground)?.let {
            foreground = ResourcesCompat.getDrawable(resources, it, context.theme)
            isClickable = true
        }

        stateListAnimator = ElevateCardViewStateListAnimator(context)

        ConstraintLayout(context).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

            imageViewPoster = ImageView(context).apply {
                scaleType = ImageView.ScaleType.CENTER_CROP
                setImageDrawable(shimmer)
            }.also(::addView)

            viewBottomScrim = View(context).apply {
                visibility = View.INVISIBLE
                setBackgroundResource(R.drawable.scrim_overlay_bottom)
            }.also(::addView)

            textViewTitle = TextView(context).apply {
                visibility = View.INVISIBLE
                setTextStyleAttr(R.attr.textAppearanceCaption)
                setTextColor(Color.WHITE)
                textAlignment = TextView.TEXT_ALIGNMENT_VIEW_END
            }.also(::addView)

            val spaceTitleTop = Space(context).also(::addView)

            viewCornerScrim = View(context).apply {
                visibility = View.INVISIBLE
                setBackgroundResource(R.drawable.scrim_overlay_corner)
            }.also(::addView)

            ratingStarView = RatingStarView(context).apply {
                textViewRating.setTextStyleAttr(R.attr.textAppearanceCaption)
                textViewRating.setTextColor(Color.WHITE)
            }.also(::addView)
            ratingStarView.visibility = View.INVISIBLE

            val spaceRatingStart = Space(context).also(::addView)

            constraintSet {
                constrain(imageViewPoster) {
                    link(start, parent.start)
                    link(end, parent.end)
                    link(top, parent.top)
                    link(bottom, parent.bottom)

                    ratio("100:148")
                }

                val textViewTitleRef = constrain(textViewTitle) {
                    link(start, parent.start, margin = 4.dp)
                    link(end, parent.end, margin = 4.dp)
                    link(bottom, parent.bottom, margin = 4.dp)

                    height(WRAP_CONTENT)
                    visibility(GONE)
                }

                val spaceTitleTopRef = constrain(spaceTitleTop) {
                    link(start, parent.start)
                    link(end, parent.end)
                    link(bottom, textViewTitleRef.top)

                    height(8.dp)
                }

                val ratingStarViewRef = constrain(ratingStarView) {
                    link(end, parent.end, margin = 4.dp)
                    link(top, parent.top, margin = 4.dp)

                    width(WRAP_CONTENT)
                    height(WRAP_CONTENT)
                    visibility(GONE)
                }

                val spaceRatingStartRef = constrain(spaceRatingStart) {
                    link(end, ratingStarViewRef.start)
                    link(top, parent.top)

                    width(24.dp)
                }

                constrain(viewBottomScrim) {
                    link(start, parent.start)
                    link(end, parent.end)
                    link(top, spaceTitleTopRef.top)
                    link(bottom, parent.bottom)

                    visibility(GONE)
                }

                constrain(viewCornerScrim) {
                    link(start, spaceRatingStartRef.start)
                    link(end, parent.end)
                    link(top, parent.top)

                    ratio("1:1")
                    visibility(GONE)
                }
            }

        }.also(::addView)
    }

    fun setRating(rating: String?) = rating?.let {
        viewCornerScrim.visibility = View.VISIBLE
        ratingStarView.visibility = View.VISIBLE
        ratingStarView.textViewRating.text = rating
    } ?: run {
        viewCornerScrim.visibility = View.GONE
        ratingStarView.visibility = View.GONE
    }

    fun setTitle(title: String?) = title?.let {
        viewBottomScrim.visibility = View.VISIBLE
        textViewTitle.visibility = View.VISIBLE
        textViewTitle.text = title
    } ?: run {
        viewBottomScrim.visibility = View.GONE
        textViewTitle.visibility = View.GONE
        textViewTitle.text = null
    }

    fun setPoster(thumbnailUrl: String?, url: String?) {
        Glide.with(this)
            .load(url)
            .thumbnail(
                Glide.with(this)
                    .load(thumbnailUrl)
            )
            .placeholder(shimmer)
            .into(imageViewPoster)
    }
}

