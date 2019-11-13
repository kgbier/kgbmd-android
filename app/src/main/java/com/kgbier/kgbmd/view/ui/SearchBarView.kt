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

    var isAnimating: Boolean = false

    fun scrollBehaviourResetPosition() {
        translationY = 0f
    }

    fun scrollBehaviourScrollDown(distance: Int) {
        animate().cancel()
        isAnimating = false
        translationY = max(scrollBehaviourLimitTopTranslation, translationY - distance)
    }

    private val interpolator = DecelerateInterpolator()
    private val animatorEndAction = Runnable { isAnimating = false }
    fun scrollBehaviourScrollUp() {
        if (isAnimating) return

        isAnimating = true
        animate().setInterpolator(interpolator).translationY(0f).withEndAction(animatorEndAction)
    }
}
