package com.kgbier.kgbmd.view

import android.annotation.SuppressLint
import android.transition.Fade
import android.transition.Transition
import android.transition.TransitionSet
import android.view.animation.DecelerateInterpolator
import android.widget.LinearLayout
import android.widget.TextView
import com.kgbier.kgbmd.MainActivity
import com.kgbier.kgbmd.TransitionRoute
import com.kgbier.kgbmd.util.sp

@SuppressLint("ViewConstructor")
class DetailLayout(context: MainActivity) : LinearLayout(context) {

    init {
        TextView(context).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            text = "This is a movie title"
            textSize = sp(42f)
        }.also(::addView)
    }
}

class DetailScreenTransitionRoute : TransitionRoute {
    override fun transition(): Transition = TransitionSet().apply {
        Fade().setInterpolator(DecelerateInterpolator()).also { addTransition(it) }
    }
}