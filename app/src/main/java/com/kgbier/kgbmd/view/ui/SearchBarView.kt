package com.kgbier.kgbmd.view.ui

import android.annotation.SuppressLint
import android.text.InputType
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.animation.DecelerateInterpolator
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.core.view.marginTop
import androidx.core.view.updateLayoutParams
import androidx.core.view.updateMarginsRelative
import com.kgbier.kgbmd.MainActivity
import com.kgbier.kgbmd.R
import com.kgbier.kgbmd.util.dp
import com.kgbier.kgbmd.util.resolveAttribute
import com.kgbier.kgbmd.util.setTextStyleAttr
import kotlin.math.max

private const val HEIGHT = 48
private const val ELEVATION = 4f

@SuppressLint("ViewConstructor")
open class SearchBarView(context: MainActivity) : CardView(context) {

    val layout: LinearLayout
    val editTextSearch: EditText
    val imageViewKeyIcon: ImageView

    private val targetMinimumHeight = HEIGHT.dp

    init {
        id = R.id.searchBarView
        isTransitionGroup = true

        radius = targetMinimumHeight / 2f
        cardElevation = ELEVATION.dp

        layout = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
            )

            imageViewKeyIcon = makeKeyIcon(context).apply {
                updateLayoutParams<LayoutParams> {
                    updateMarginsRelative(end = 4.dp)
                }
            }.also(::addView)

            editTextSearch = EditText(context).apply {
                id = generateViewId()
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    LayoutParams.MATCH_PARENT,
                    1f
                ).apply {
                    marginEnd = 12.dp
                }

                setTextStyleAttr(R.attr.textAppearanceSubtitle1)
                inputType = InputType.TYPE_CLASS_TEXT
                background = null
                maxLines = 1
            }.also(::addView)
        }.also(::addView)
    }

    fun makeKeyIcon(context: MainActivity) = ImageView(context).apply {
        layoutParams = LayoutParams(
            targetMinimumHeight,
            targetMinimumHeight
        )
        scaleType = ImageView.ScaleType.CENTER_INSIDE
        setImageResource(R.drawable.ic_search)
    }

    private var _barAddons: List<View>? = null
    var barAddons: List<View>
        set(value) {
            if (_barAddons != null) throw IllegalStateException("Addons can only be set once.")

            editTextSearch.updateLayoutParams<MarginLayoutParams> {
                updateMarginsRelative(end = 4.dp)
            }
            value.forEach {
                View(context).apply {
                    layoutParams = MarginLayoutParams(
                        1.dp,
                        MATCH_PARENT
                    ).apply {
                        updateMarginsRelative(top = 8.dp, bottom = 8.dp)
                        resolveAttribute(android.R.attr.colorForeground)?.let(::setBackgroundResource)
                    }
                }.also(layout::addView)
                layout.addView(it)
            }
            _barAddons = value
        }
        get() = _barAddons ?: emptyList()

    private var isAnimating = false
    private var scrollBehaviourTopTranslationLimit = 0f

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        scrollBehaviourTopTranslationLimit = (measuredHeight + marginTop + 8.dp) * -1f
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
