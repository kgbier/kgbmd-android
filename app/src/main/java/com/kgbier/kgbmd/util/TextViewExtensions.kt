package com.kgbier.kgbmd.util

import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.TextViewCompat

fun TextView.setTextStyle(@StyleRes resId: Int) = TextViewCompat.setTextAppearance(this, resId)
fun TextView.setTextColorAttr(@AttrRes resId: Int) = resolveAttribute(resId)?.let {
    setTextColor(ResourcesCompat.getColor(resources, it, context.theme))
}