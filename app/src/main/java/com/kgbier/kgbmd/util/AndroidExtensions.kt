package com.kgbier.kgbmd.util

import android.content.Context
import android.util.TypedValue
import android.view.View
import androidx.annotation.AttrRes
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun Context.dp(displayPixels: Float): Float =
    resources.displayMetrics.density * displayPixels

fun Context.dp(displayPixels: Int): Int =
    (resources.displayMetrics.density * displayPixels).toInt()

fun View.dp(displayPixels: Float): Float =
    resources.displayMetrics.density * displayPixels

fun View.dp(displayPixels: Int): Int =
    (resources.displayMetrics.density * displayPixels).toInt()

fun Context.resolveAttribute(@AttrRes resource: Int): Int? = with(TypedValue()) {
    if (theme.resolveAttribute(resource, this, true)) {
        return resourceId
    } else null
}

fun View.resolveAttribute(@AttrRes resource: Int): Int? = context.resolveAttribute(resource)

inline fun <T> LiveData<T>.bind(
    lifecycleOwner: LifecycleOwner,
    crossinline observerClosure: (T) -> Unit
) = this.observe(lifecycleOwner, Observer<T> { observerClosure.invoke(it) })