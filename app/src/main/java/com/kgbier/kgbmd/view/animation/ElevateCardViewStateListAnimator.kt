package com.kgbier.kgbmd.view.animation

import android.animation.ObjectAnimator
import android.animation.StateListAnimator
import android.content.Context
import com.kgbier.kgbmd.util.dp

class ElevateCardViewStateListAnimator(context: Context) : StateListAnimator() {

    private val animationTime =
        context.resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
    private val elevationPropertyName = "cardElevation"

    private val downAnimator = ObjectAnimator().apply {
        duration = animationTime
        setPropertyName(elevationPropertyName)
        setFloatValues(context.dp(8f))
    }

    private val upAnimator = ObjectAnimator().apply {
        duration = animationTime
        setPropertyName(elevationPropertyName)
        setFloatValues(context.dp(1f))
    }

    init {
        this.addState(
            listOf(android.R.attr.state_enabled, android.R.attr.state_pressed).toIntArray(),
            downAnimator
        )
        this.addState(
            listOf(android.R.attr.state_enabled).toIntArray(),
            upAnimator
        )
    }
}