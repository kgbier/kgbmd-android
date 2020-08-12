package com.kgbier.kgbmd.view.behaviour

import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlin.math.absoluteValue

class ScrollBehaviour<T> : CoordinatorLayout.Behavior<T>()
        where T : View, T : ScrollBehaviour.Child {

    interface Child {
        fun scrollBehaviourResetPosition()
        fun scrollBehaviourScrollDown(distance: Int, totalDistance: Int)
        fun scrollBehaviourScrollUp(distance: Int, totalDistance: Int)
    }

    var totalDy = 0

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: T,
        dependency: View
    ): Boolean =
        dependency is SwipeRefreshLayout

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: T,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean = axes and ViewCompat.SCROLL_AXIS_VERTICAL != 0

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: T,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int
    ) {
        if (dyConsumed == 0) return
        totalDy += dyConsumed

        val reachedTop = dyUnconsumed != 0 && dyUnconsumed < 0
        if (reachedTop) {
            child.scrollBehaviourResetPosition()
            return
        }

        val goingDown = dyConsumed > 0
        if (goingDown) child.scrollBehaviourScrollDown(dyConsumed, totalDy)
        else child.scrollBehaviourScrollUp(-dyConsumed, totalDy)
    }
}
