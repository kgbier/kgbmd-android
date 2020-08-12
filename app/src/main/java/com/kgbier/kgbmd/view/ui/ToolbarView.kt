package com.kgbier.kgbmd.view.ui

import android.animation.ValueAnimator
import android.content.Context
import androidx.appcompat.widget.Toolbar
import com.kgbier.kgbmd.R
import com.kgbier.kgbmd.util.dp
import com.kgbier.kgbmd.util.resolveAttribute
import com.kgbier.kgbmd.view.behaviour.ScrollBehaviour

class ToolbarView(context: Context) : Toolbar(context), ScrollBehaviour.Child {

    val liftAnimator: ValueAnimator
    val liftTriggerDistance = 32.dp
    val elevationAnimDuration = 150L

    init {
        resolveAttribute(R.attr.colorBackgroundFloating)?.let { setBackgroundResource(it) }
        elevation = 4f.dp

        liftAnimator = ValueAnimator().apply {
            duration = elevationAnimDuration
            setIntValues(0, 255)
            addUpdateListener { background.alpha = it.animatedValue as Int }
        }

        background.alpha = 0
    }

    var isLifted = false

    override fun scrollBehaviourResetPosition() {
        isLifted = false
        background.alpha = 0
    }

    override fun scrollBehaviourScrollDown(distance: Int, totalDistance: Int) {
        if (!isLifted) {
            if (totalDistance > liftTriggerDistance) {
                isLifted = true
                liftAnimator.start()
            }
        }
    }

    override fun scrollBehaviourScrollUp(distance: Int, totalDistance: Int) {
        if (isLifted) {
            if (totalDistance < liftTriggerDistance || (distance - totalDistance - liftTriggerDistance) > 0) {
                isLifted = false
                liftAnimator.reverse()
            }
        }
    }
}
