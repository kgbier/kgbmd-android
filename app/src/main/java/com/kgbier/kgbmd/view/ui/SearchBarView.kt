package com.kgbier.kgbmd.view.ui

import android.annotation.SuppressLint
import android.text.InputType
import android.view.animation.DecelerateInterpolator
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.core.view.marginTop
import androidx.core.widget.TextViewCompat
import com.kgbier.kgbmd.MainActivity
import com.kgbier.kgbmd.R
import com.kgbier.kgbmd.util.dp
import kotlin.math.max

private const val HEIGHT = 48
private const val ELEVATION = 4f

@SuppressLint("ViewConstructor")
open class SearchBarView(context: MainActivity) : CardView(context) {

    val editTextSearch: EditText
    val imageViewKeyIcon: ImageView

    private val targetMinimumHeight = dp(HEIGHT)

    init {
        id = R.id.searchBarView
        isTransitionGroup = true

        radius = targetMinimumHeight / 2f
        cardElevation = dp(ELEVATION)

        LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
            ).apply {
                marginEnd = dp(12)
            }

            imageViewKeyIcon = ImageView(context).apply {
                layoutParams = LayoutParams(
                    targetMinimumHeight,
                    targetMinimumHeight
                ).apply {
                    marginEnd = dp(4)
                }
                scaleType = ImageView.ScaleType.CENTER_INSIDE
                setImageResource(R.drawable.ic_search)
            }.also(::addView)

            editTextSearch = EditText(context).apply {
                id = generateViewId()
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    LayoutParams.MATCH_PARENT,
                    1f
                )
                TextViewCompat.setTextAppearance(
                    this,
                    android.R.style.TextAppearance_Material_Subhead
                )
                inputType = InputType.TYPE_CLASS_TEXT
                background = null
                maxLines = 1
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

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(
            widthMeasureSpec,
            MeasureSpec.makeMeasureSpec(targetMinimumHeight, MeasureSpec.EXACTLY)
        )
    }
}
