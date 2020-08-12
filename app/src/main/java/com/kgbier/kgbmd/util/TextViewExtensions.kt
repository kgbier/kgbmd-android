package com.kgbier.kgbmd.util

import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.annotation.StyleRes
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat

fun TextView.setTextStyle(@StyleRes resId: Int) =
    TextViewCompat.setTextAppearance(this, resId)

fun TextView.setTextStyleAttr(@AttrRes resId: Int) =
    resolveAttribute(resId)?.let(this::setTextStyle)

fun TextView.setTextColorAttr(@AttrRes resId: Int) =
    resolveAttribute(resId)?.let { setTextColorResource(it) }

fun TextView.setTextColorResource(@ColorRes resId: Int) =
    setTextColor(ContextCompat.getColor(context, resId))
