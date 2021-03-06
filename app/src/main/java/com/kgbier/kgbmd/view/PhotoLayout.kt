package com.kgbier.kgbmd.view

import android.annotation.SuppressLint
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.transition.*
import com.bumptech.glide.Glide
import com.kgbier.kgbmd.*
import com.kgbier.kgbmd.R

@SuppressLint("ViewConstructor")
class PhotoLayout(context: MainActivity) :
    RouteReceiver<Route.PhotoScreen> by Navigation.routeReceiver(),
    LinearLayout(context) {

    val imageViewPhoto: ImageView

    init {
        setBackgroundResource(R.color.black)

        imageViewPhoto = ImageView(context).apply {
            id = R.id.imageViewPhoto
            layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
        }.also(::addView)

        Glide.with(this)
            .load(route.fullImage)
            .into(imageViewPhoto)
    }
}

class PhotoScreenTransitionRoute : TransitionRoute {
    override fun transition(): Transition = TransitionSet().apply {
        Fade()
            .setInterpolator(DecelerateInterpolator())
            .also { addTransition(it) }

        ChangeBounds()
            .addTarget(R.id.imageViewPhoto)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .also { addTransition(it) }

        ChangeImageTransform()
            .addTarget(R.id.imageViewPhoto)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .also { addTransition(it) }
    }
}
