package com.kgbier.kgbmd.view.ui.listingdetails

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet.WRAP_CONTENT
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.kgbier.kgbmd.R
import com.kgbier.kgbmd.domain.model.TitleDetails
import com.kgbier.kgbmd.util.*
import com.kgbier.kgbmd.view.component.ListingTitle
import com.kgbier.kgbmd.view.drawable.ShimmerDrawable
import com.kgbier.kgbmd.view.ui.HeroRatingView

class HeaderView(context: Context) : ConstraintLayout(context) {

    val imageViewBackground: ImageView
    val viewScrim: View
    val listingTitle: ListingTitle
    val heroRatingView: HeroRatingView

    init {
        imageViewBackground = ImageView(context).apply {
            scaleType = ImageView.ScaleType.CENTER_CROP
        }.also(::addView)
        viewScrim = ImageView(context).apply {
            background = ContextCompat.getDrawable(context, R.drawable.scrim_overlay)
        }.also(::addView)
        listingTitle = ListingTitle(context).also(::addView)
        heroRatingView = HeroRatingView(context).also(::addView)

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

            val heroRatingViewRef = ref(heroRatingView)
            val listingTitleRef = ref(listingTitle)

            constrain(listingTitleRef) {
                link(start, parent.start, margin = 16.dp)
                link(top, parent.top, margin = 16.dp)
                link(end, parent.end, margin = 16.dp)
                link(bottom, heroRatingViewRef.top, 16.dp)

                height(WRAP_CONTENT)
            }

            constrain(heroRatingViewRef) {
                link(top, listingTitleRef.bottom)
                link(end, parent.end, margin = 16.dp)
                link(bottom, parent.bottom, margin = 12.dp)

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
) : BaseListingViewModel

class HeaderViewHolder(context: Context) : BaseListingViewHolder(HeaderView(context).apply {
    layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
}) {
    val view get() = itemView as HeaderView

    val shimmer = ShimmerDrawable()

    override fun bind(viewModel: BaseListingViewModel) {
        if (viewModel !is HeaderViewModel) return

        Glide.with(view)
            .load(viewModel.poster?.largeUrl)
            .thumbnail(
                Glide.with(view)
                    .load(viewModel.poster?.thumbnailUrl)
            )
            .placeholder(shimmer)
            .into(view.imageViewBackground)

        view.listingTitle.setTitleSequence(viewModel.name, viewModel.yearReleased)

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
