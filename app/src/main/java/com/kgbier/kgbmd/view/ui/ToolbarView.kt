package com.kgbier.kgbmd.view.ui

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import androidx.appcompat.widget.Toolbar
import androidx.core.graphics.ColorUtils
import com.kgbier.kgbmd.R
import com.kgbier.kgbmd.util.dp
import com.kgbier.kgbmd.util.resolveAttribute
import com.kgbier.kgbmd.util.resolveColorAttribute
import com.kgbier.kgbmd.view.behaviour.ScrollBehaviour

class ToolbarView(context: Context) : Toolbar(context), ScrollBehaviour.Child {

    val liftAlphaAnimator: ValueAnimator
    val liftTriggerDistance = 32.dp
    val elevationAnimDuration = 150L

    var baseTitleColor = resolveColorAttribute(android.R.attr.textColorPrimary) ?: Color.WHITE

    init {
        resolveAttribute(R.attr.backgroundColorSecondary)?.let(::setBackgroundResource)
        elevation = 4f.dp

        liftAlphaAnimator = ValueAnimator().apply {
            duration = elevationAnimDuration
            setIntValues(0, 255)
            addUpdateListener { setToolbarAlpha(it.animatedValue as Int) }
        }

        liftAlphaAnimator.reverse()
        liftAlphaAnimator.end()
    }

    private fun setToolbarAlpha(alpha: Int) {
        background.alpha = alpha
        setTitleTextColor(ColorUtils.setAlphaComponent(baseTitleColor, alpha))
    }

    private var isLifted = false

    override fun scrollBehaviourResetPosition() {
        isLifted = false
        liftAlphaAnimator.cancel()
        setToolbarAlpha(0)
    }

    override fun scrollBehaviourScrollDown(distance: Int, totalDistance: Int) {
        if (!isLifted) {
            if (totalDistance > liftTriggerDistance) {
                isLifted = true
                liftAlphaAnimator.start()
            }
        }
    }

    override fun scrollBehaviourScrollUp(distance: Int, totalDistance: Int) {
        if (isLifted) {
            if (totalDistance < liftTriggerDistance || (distance - totalDistance - liftTriggerDistance) > 0) {
                isLifted = false
                liftAlphaAnimator.reverse()
            }
        }
    }
}
