package com.kgbier.kgbmd.util

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import androidx.annotation.AttrRes
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

/**
 * `dp` to pixels
 */

fun Context.dp(displayPixels: Float): Float =
    resources.displayMetrics.density * displayPixels

fun Context.dp(displayPixels: Int): Int =
    (resources.displayMetrics.density * displayPixels).toInt()

fun View.dp(displayPixels: Float): Float =
    resources.displayMetrics.density * displayPixels

fun View.dp(displayPixels: Int): Int =
    (resources.displayMetrics.density * displayPixels).toInt()

fun Int.dp(): Int =
    (Resources.getSystem().displayMetrics.density * this).toInt()

fun Float.dp(): Float =
    (Resources.getSystem().displayMetrics.density * this)

/**
 * `sp` to pixels
 */

fun Context.sp(scaledPixels: Float): Float =
    resources.displayMetrics.scaledDensity * scaledPixels

fun Context.sp(scaledPixels: Int): Int =
    (resources.displayMetrics.scaledDensity * scaledPixels).toInt()

fun View.sp(scaledPixels: Float): Float =
    resources.displayMetrics.scaledDensity * scaledPixels

fun View.sp(scaledPixels: Int): Int =
    (resources.displayMetrics.scaledDensity * scaledPixels).toInt()

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
 * LiveData
 */

typealias LiveDataDisposable = () -> Unit

class LiveDataDisposeBag {
    private val disposables = mutableListOf<LiveDataDisposable>()

    fun add(liveDataDisposable: LiveDataDisposable) {
        disposables.add(liveDataDisposable)
    }

    fun dispose() {
        disposables.forEach { it.invoke() }
        disposables.clear()
    }
}

inline fun <T> LiveData<T>.bind(
    lifecycleOwner: LifecycleOwner,
    crossinline observerClosure: (T) -> Unit
): LiveDataDisposable {
    val observer = Observer<T> { observerClosure.invoke(it) }
    observe(lifecycleOwner, observer)
    return { removeObserver(observer) }
}

fun LiveDataDisposable.disposeBy(disposeBag: LiveDataDisposeBag) = disposeBag.add(this)

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

fun View.requestApplyInsetsWhenAttached() {
    if (isAttachedToWindow) {
        // We're already attached, just request as normal
        requestApplyInsets()
    } else {
        // We're not attached to the hierarchy, add a listener to
        // request when we are
        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                v.removeOnAttachStateChangeListener(this)
                v.requestApplyInsets()
            }

            override fun onViewDetachedFromWindow(v: View) = Unit
        })
    }
}