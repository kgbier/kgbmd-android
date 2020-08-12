package com.kgbier.kgbmd.util

import android.content.Context
import android.text.Spannable.SPAN_INCLUSIVE_EXCLUSIVE
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.TextAppearanceSpan
import androidx.annotation.ColorRes
import androidx.annotation.StyleRes
import androidx.core.content.ContextCompat

class SpannableStringBuilderScope(val builder: SpannableStringBuilder, val context: Context)

inline fun spannableStringBuilder(
    context: Context,
    block: SpannableStringBuilderScope.() -> Unit
): SpannableStringBuilder {
    val scope = SpannableStringBuilderScope(SpannableStringBuilder(), context)
    scope.block()
    return scope.builder
}

inline fun SpannableStringBuilderScope.withTextAppearance(
    @StyleRes style: Int? = null,
    @ColorRes colour: Int? = null,
    block: SpannableStringBuilderScope.() -> Unit
) {
    val start = builder.length
    block()
    val end = builder.length
    style?.let {
        val textAppearanceSpan = TextAppearanceSpan(context, it)
        builder.setSpan(textAppearanceSpan, start, end, SPAN_INCLUSIVE_EXCLUSIVE)
    }
    colour?.let {
        val foregroundColorSpan = ForegroundColorSpan(ContextCompat.getColor(context, it))
        builder.setSpan(foregroundColorSpan, start, end, SPAN_INCLUSIVE_EXCLUSIVE)
    }
}

inline fun SpannableStringBuilderScope.append(text: String) {
    builder.append(text)
}
