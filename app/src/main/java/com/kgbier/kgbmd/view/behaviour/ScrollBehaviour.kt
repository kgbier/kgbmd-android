package com.kgbier.kgbmd.view.behaviour

import android.content.Context
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.core.view.marginTop
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.kgbier.kgbmd.util.dp
import kotlin.math.max
import kotlin.math.min


class ScrollBehaviour<V : View>(private val context: Context) : CoordinatorLayout.Behavior<V>() {

    var limitTopTranslation = 0f

    override fun layoutDependsOn(parent: CoordinatorLayout, child: V, dependency: View): Boolean =
        dependency is SwipeRefreshLayout

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean = axes and ViewCompat.SCROLL_AXIS_VERTICAL != 0

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int
    ) {
        val reachedTop = dyUnconsumed != 0 && dyUnconsumed < 0
        if (reachedTop) {
            child.translationY = 0f; return
        }

        val goingDown = dyConsumed > 0

        val oldTranslation = child.translationY
        child.translationY = if (goingDown) {
            max(limitTopTranslation, oldTranslation - dyConsumed)
        } else {
            min(0f, oldTranslation - dyConsumed)
        }
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: V,
        dependency: View
    ): Boolean {
        limitTopTranslation = (child.measuredHeight + child.marginTop + context.dp(8)) * -1f
        return false
    }
}