package com.kgbier.kgbmd.view.ui

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.text.InputType
import android.view.Gravity
import android.widget.EditText
import androidx.cardview.widget.CardView
import androidx.core.view.marginTop
import com.kgbier.kgbmd.MainActivity
import com.kgbier.kgbmd.util.dp
import kotlin.math.max
import kotlin.math.min

private const val HEIGHT = 40
private const val ELEVATION = 4f

@SuppressLint("ViewConstructor")
open class SearchBarView(context: MainActivity) : CardView(context) {

    var scrollBehaviourLimitTopTranslation = 0f

    val editTextSearch: EditText

    init {
        minimumHeight = dp(HEIGHT)
        radius = minimumHeight / 2f
        cardElevation = dp(ELEVATION)

        editTextSearch = EditText(context).apply {
            inputType = InputType.TYPE_CLASS_TEXT
            background = null
            setPaddingRelative(0, dp(8), 0, dp(8))
            maxLines = 1
            layoutParams =
                LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT,
                    Gravity.CENTER_VERTICAL
                ).apply {
                    marginStart = dp(12)
                    marginEnd = dp(12)
                }
        }.also(::addView)
    }

    fun setHint(hint: String) {
        editTextSearch.hint = hint
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        scrollBehaviourLimitTopTranslation = (measuredHeight + marginTop + dp(8)) * -1f
    }

    var animator: ValueAnimator? = null

    fun scrollBehaviourResetPosition() {
        if (translationY == 0f) return

        translationY = 0f
    }

    fun scrollBehaviourScrollDown(distance: Int) {
        translationY = max(scrollBehaviourLimitTopTranslation, translationY - distance)
    }

    fun scrollBehaviourScrollUp(distance: Int) {
        translationY = min(0f, translationY - distance)
    }
}
