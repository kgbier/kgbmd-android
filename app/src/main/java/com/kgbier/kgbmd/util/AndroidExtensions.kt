package com.kgbier.kgbmd.util

import android.content.Context
import android.util.TypedValue
import android.view.View
import androidx.annotation.AttrRes

fun Context.dp(displayPixels: Float): Float =
    resources.displayMetrics.density * displayPixels

fun Context.dp(displayPixels: Int): Int =
    (resources.displayMetrics.density * displayPixels).toInt()

fun View.dp(displayPixels: Float): Float =
    resources.displayMetrics.density * displayPixels

fun View.dp(displayPixels: Int): Int =
    (resources.displayMetrics.density * displayPixels).toInt()

fun View.resolveAttribute(@AttrRes resource: Int): Int? = with(TypedValue()) {
    if(context.theme.resolveAttribute(resource, this, true)) {
        return resourceId
    } else null
}
