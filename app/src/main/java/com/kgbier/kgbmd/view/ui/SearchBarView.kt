package com.kgbier.kgbmd.view.ui

import android.annotation.SuppressLint
import android.text.InputType
import android.view.animation.DecelerateInterpolator
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.core.view.marginTop
import com.kgbier.kgbmd.MainActivity
import com.kgbier.kgbmd.R
import com.kgbier.kgbmd.util.dp
import kotlin.math.max

private const val HEIGHT = 40
private const val ELEVATION = 4f

@SuppressLint("ViewConstructor")
open class SearchBarView(context: MainActivity) : CardView(context) {

    val editTextSearch: EditText

    init {
        id = R.id.searchBarView
        isTransitionGroup = true

        minimumHeight = dp(HEIGHT)
        radius = minimumHeight / 2f
        cardElevation = dp(ELEVATION)

        LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
            ).apply {
                marginStart = dp(12)
                marginEnd = dp(12)
            }

            ImageView(context).apply {
                layoutParams = LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.MATCH_PARENT
                )
                setImageResource(R.drawable.ic_search)
            }.also(::addView)

            editTextSearch = EditText(context).apply {
                inputType = InputType.TYPE_CLASS_TEXT
                background = null
                maxLines = 1
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    LayoutParams.WRAP_CONTENT,
                    1f
                )
            }.also(::addView)
        }.also(::addView)
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
