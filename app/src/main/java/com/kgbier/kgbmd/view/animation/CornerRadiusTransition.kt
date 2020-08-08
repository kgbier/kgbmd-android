package com.kgbier.kgbmd.view.animation

import android.animation.Animator
import android.animation.ObjectAnimator
import androidx.transition.Transition
import androidx.transition.TransitionValues
import android.view.ViewGroup
import androidx.cardview.widget.CardView

class CornerRadiusTransition : Transition() {

    companion object {
        const val PROPNAME_RADIUS = "kgbmd:radiusTransition:radius"
    }

    override fun captureStartValues(transitionValues: TransitionValues) =
        captureValues(transitionValues)

    override fun captureEndValues(transitionValues: TransitionValues) =
        captureValues(transitionValues)

    private fun captureValues(transitionValues: TransitionValues) {
        val view = transitionValues.view as? CardView ?: return
        transitionValues.values[PROPNAME_RADIUS] = view.radius
    }

    override fun createAnimator(
        sceneRoot: ViewGroup,
        startValues: TransitionValues?,
        endValues: TransitionValues?
    ): Animator? {
        startValues ?: return null
        endValues ?: return null

        return ObjectAnimator().apply {
            target = endValues.view
            setPropertyName("radius")
            duration =
                sceneRoot.resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
            setFloatValues(
                (startValues.values[PROPNAME_RADIUS] as? Float) ?: 0f,
                (endValues.values[PROPNAME_RADIUS] as? Float) ?: 0f
            )
        }
    }

}
