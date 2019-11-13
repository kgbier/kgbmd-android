package com.kgbier.kgbmd.view.behaviour

import android.content.Context
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.kgbier.kgbmd.view.ui.SearchBarView

class ScrollBehaviour(private val context: Context) :
    CoordinatorLayout.Behavior<SearchBarView>() {

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: SearchBarView,
        dependency: View
    ): Boolean =
        dependency is SwipeRefreshLayout

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: SearchBarView,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean = axes and ViewCompat.SCROLL_AXIS_VERTICAL != 0

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: SearchBarView,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int
    ) {
        val reachedTop = dyUnconsumed != 0 && dyUnconsumed < 0
        if (reachedTop) {
            child.scrollBehaviourResetPosition()
            return
        }

        val goingDown = dyConsumed > 0
        if (goingDown) child.scrollBehaviourScrollDown(dyConsumed)
        else child.scrollBehaviourScrollUp(dyConsumed)
    }
}