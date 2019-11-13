package com.kgbier.kgbmd.view.ui

import android.annotation.SuppressLint
import android.text.InputType
import android.view.Gravity
import android.view.animation.DecelerateInterpolator
import android.widget.EditText
import androidx.cardview.widget.CardView
import androidx.core.view.marginTop
import com.kgbier.kgbmd.MainActivity
import com.kgbier.kgbmd.util.dp
import kotlin.math.max

private const val HEIGHT = 40
private const val ELEVATION = 4f

@SuppressLint("ViewConstructor")
open class SearchBarView(context: MainActivity) : CardView(context) {

    val editTextSearch: EditText

    init {
        minimumHeight = dp(HEIGHT)
        radius = minimumHeight / 2f
        cardElevation = dp(ELEVATION)

        editTextSearch = EditText(context).apply {
            inputType = InputType.TYPE_CLASS_TEXT
            isEnabled = false
            background = null
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

    private var isAnimating = false
    private var scrollBehaviourTopTranslationLimit = 0f

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        scrollBehaviourTopTranslationLimit = (measuredHeight + marginTop + dp(8)) * -1f
    }

    fun scrollBehaviourResetPosition() {
        translationY = 0f
    }

    fun scrollBehaviourScrollDown(distance: Int) {
        animate().cancel()
        isAnimating = false
        translationY = max(scrollBehaviourTopTranslationLimit, translationY - distance)
    }

    private val interpolator by lazy { DecelerateInterpolator() }
    private val animatorEndAction by lazy { Runnable { isAnimating = false } }
    fun scrollBehaviourScrollUp() {
        if (isAnimating) return

        isAnimating = true
        animate().setInterpolator(interpolator).translationY(0f).withEndAction(animatorEndAction)
    }
}
