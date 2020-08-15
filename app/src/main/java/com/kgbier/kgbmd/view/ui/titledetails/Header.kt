package com.kgbier.kgbmd.view.ui.titledetails

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Space
import androidx.appcompat.view.ContextThemeWrapper
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet.WRAP_CONTENT
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.kgbier.kgbmd.R
import com.kgbier.kgbmd.domain.model.TitleDetails
import com.kgbier.kgbmd.util.*
import com.kgbier.kgbmd.view.component.TitleHeading
import com.kgbier.kgbmd.view.ui.HeroRatingView

class HeaderView(context: ContextThemeWrapper) : ConstraintLayout(context) {

    // Wrap this view with the Dark theme
    constructor(context: Context) : this(ContextThemeWrapper(context, R.style.BaseAlphaTheme_Dark))

    val spaceTop: Space
    val imageViewBackground: ImageView
    val viewScrim: View
    val titleHeading: TitleHeading
    val heroRatingView: HeroRatingView
    val imageViewPoster: ImageView

    init {
        val defaultSpaceTopHeight = resolveDimensionAttribute(android.R.attr.actionBarSize) ?: 0

        spaceTop = Space(context).also(::addView)
        imageViewBackground = ImageView(context).apply {
            scaleType = ImageView.ScaleType.CENTER_CROP
        }.also(::addView)
        viewScrim = ImageView(context).apply {
            background = ContextCompat.getDrawable(context, R.drawable.scrim_overlay)
        }.also(::addView)
        titleHeading = TitleHeading(context).also(::addView)
        heroRatingView = HeroRatingView(context).also(::addView)
        imageViewPoster = ImageView(context).apply {
            scaleType = ImageView.ScaleType.CENTER_CROP
        }.also(::addView)

        setOnUpdateWithWindowInsetsListener { _, insets, _, _ ->
            constraintSet {
                constrain(spaceTop) {
                    height(defaultSpaceTopHeight + insets.systemWindowInsetTop)
                }
            }
            insets.consumeSystemWindowInsets()
        }

        constraintSet {
            constrain(imageViewBackground) {
                link(start, parent.start)
                link(top, parent.top)
                link(end, parent.end)
                link(bottom, parent.bottom)
            }
            constrain(viewScrim) {
                link(start, parent.start)
                link(top, parent.top)
                link(end, parent.end)
                link(bottom, parent.bottom)
            }
            val spaceTopRef = constrain(spaceTop) {
                link(start, parent.start)
                link(top, parent.top)
                link(end, parent.end)

                // This will update once window insets have been consumed
                height(
                    defaultSpaceTopHeight + (WindowInsetsHint.lastInsets?.systemWindowInsetTop ?: 0)
                )
            }

            val heroRatingViewRef = ref(heroRatingView)
            val titleHeadingRef = ref(titleHeading)
            val imageViewPosterRef = ref(imageViewPoster)

            constrain(titleHeadingRef) {
                link(start, parent.start, margin = 16.dp)
                link(top, spaceTopRef.bottom, margin = 16.dp)
                link(end, parent.end, margin = 16.dp)
                link(bottom, imageViewPosterRef.top, margin = 16.dp)

                height(WRAP_CONTENT)
            }

            constrain(imageViewPosterRef) {
                link(top, titleHeadingRef.bottom)
                link(end, parent.end, margin = 16.dp)
                link(bottom, parent.bottom, margin = 12.dp)

                minHeight(62.dp)
                ratio("100:148")
            }

            constrain(heroRatingViewRef) {
                link(top, imageViewPosterRef.top)
                link(bottom, imageViewPosterRef.bottom)
                link(end, imageViewPosterRef.start, margin = 16.dp)

                width(WRAP_CONTENT)
                height(WRAP_CONTENT)
            }
        }
    }
}

class HeaderViewModel(
    val name: String,
    val yearReleased: String?,
    val rating: TitleDetails.Rating?,
    val poster: TitleDetails.Poster?
) : BaseTitlesViewModel

class HeaderViewHolder(context: Context) : BaseTitlesViewHolder(HeaderView(context).apply {
    layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
}) {
    val view get() = itemView as HeaderView

    override fun bind(viewModel: BaseTitlesViewModel) {
        if (viewModel !is HeaderViewModel) return

        if(viewModel.poster == null) {
            view.imageViewPoster.visibility = View.GONE
        } else {
            view.imageViewPoster.visibility = View.VISIBLE
            val posterHint = Glide.with(view)
                .load(viewModel.poster?.hintUrl)

            Glide.with(view)
                .load(viewModel.poster?.largeUrl)
                .thumbnail(posterHint)
                .into(view.imageViewBackground)

            Glide.with(view)
                .load(viewModel.poster?.thumbnailUrl)
                .thumbnail(posterHint)
                .into(view.imageViewPoster)
        }
        view.titleHeading.setTitleSequence(viewModel.name, viewModel.yearReleased)

        if (viewModel.rating == null) {
            view.heroRatingView.visibility = View.GONE
        } else {
            view.heroRatingView.visibility = View.VISIBLE
            view.heroRatingView.setRating(
                viewModel.rating.value,
                viewModel.rating.best,
                viewModel.rating.count
            )
        }
    }
}
