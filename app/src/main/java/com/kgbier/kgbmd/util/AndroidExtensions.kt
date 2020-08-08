package com.kgbier.kgbmd.util

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import androidx.annotation.AttrRes

/**
 * `dp` to pixels
 */

val Int.dp: Int
    get() = (Resources.getSystem().displayMetrics.density * this).toInt()

val Float.dp: Float
    get() = (Resources.getSystem().displayMetrics.density * this)

/**
 * Resource attribute (attr) resolution
 */

fun Context.resolveAttribute(@AttrRes resource: Int): Int? = with(TypedValue()) {
    if (theme.resolveAttribute(resource, this, true)) {
        return resourceId
    } else null
}

fun View.resolveAttribute(@AttrRes resource: Int): Int? = context.resolveAttribute(resource)

/**
 * System window inset
 */

data class LayoutAdjustment(
    val start: Int = 0,
    val top: Int = 0,
    val end: Int = 0,
    val bottom: Int = 0
)

// Stolen in part from: https://medium.com/androiddevelopers/windowinsets-listeners-to-layouts-8f9ccc8fa4d1
inline fun View.setOnUpdateWithWindowInsetsListener(
    crossinline onApply: (
        view: View,
        insets: WindowInsets,
        intendedPadding: LayoutAdjustment,
        intendedMargin: LayoutAdjustment
    ) -> Unit
) {
    val intendedPadding = LayoutAdjustment(
        paddingStart,
        paddingTop,
        paddingEnd,
        paddingBottom
    )

    val intendedMargin = (layoutParams as? ViewGroup.MarginLayoutParams)?.let {
        LayoutAdjustment(
            it.marginEnd,
            it.topMargin,
            it.marginEnd,
            it.bottomMargin
        )
    } ?: LayoutAdjustment()

    setOnApplyWindowInsetsListener { v, insets ->
        onApply(v, insets, intendedPadding, intendedMargin)
        insets
    }
    requestApplyInsetsWhenAttached()
}

fun View.requestApplyInsetsWhenAttached() = if (isAttachedToWindow) {
    // We're already attached, just request as normal
    requestApplyInsets()
} else {
    // We're not attached to the hierarchy, add a listener to
    // request when we are
    addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
        override fun onViewAttachedToWindow(view: View) {
            view.removeOnAttachStateChangeListener(this)
            view.requestApplyInsets()
        }

        override fun onViewDetachedFromWindow(view: View) = Unit
    })
}
