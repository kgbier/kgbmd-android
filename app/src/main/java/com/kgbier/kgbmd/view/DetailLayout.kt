package com.kgbier.kgbmd.view

import android.annotation.SuppressLint
import android.transition.Fade
import android.transition.Transition
import android.transition.TransitionSet
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.animation.DecelerateInterpolator
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.updatePadding
import androidx.core.view.updatePaddingRelative
import com.kgbier.kgbmd.MainActivity
import com.kgbier.kgbmd.R
import com.kgbier.kgbmd.TransitionRoute
import com.kgbier.kgbmd.util.dp
import com.kgbier.kgbmd.util.setOnUpdateWithWindowInsetsListener
import com.kgbier.kgbmd.util.setTextColorAttr
import com.kgbier.kgbmd.util.setTextStyle

@SuppressLint("ViewConstructor")
class DetailLayout(context: MainActivity) : LinearLayout(context) {

    init {
        orientation = VERTICAL

        updatePaddingRelative(start = 16.dp(), end = 16.dp(), top = 16.dp(), bottom = 16.dp())

        setOnUpdateWithWindowInsetsListener { _, insets, intendedPadding, _ ->
            updatePadding(
                top = intendedPadding.top + insets.systemWindowInsetTop,
                bottom = intendedPadding.bottom + insets.systemWindowInsetBottom
            )
            insets.consumeSystemWindowInsets()
        }

        TextView(context).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            text = "Star Wars: Episode VIII - The Last Jedi"
            setTextStyle(R.style.TextAppearance_MaterialComponents_Headline5)
            setTextColorAttr(android.R.attr.textColorPrimary)
        }.also(::addView)

        LinearLayout(context).apply {
            orientation = HORIZONTAL
            layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
                topMargin = 12.dp()
            }

            TextView(context).apply {
                layoutParams =
                    LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
                        marginEnd = 8.dp()
                    }
                text = "2017"
                setTextStyle(R.style.TextAppearance_MaterialComponents_Overline)
                setTextColorAttr(android.R.attr.textColorPrimary)
            }.also(::addView)

            TextView(context).apply {
                layoutParams =
                    LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
                        marginEnd = 8.dp()
                    }
                text = "M"
                setTextStyle(R.style.TextAppearance_MaterialComponents_Overline)
                setTextColorAttr(android.R.attr.textColorPrimary)
            }.also(::addView)

            TextView(context).apply {
                layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
                text = "2H 32M"
                setTextStyle(R.style.TextAppearance_MaterialComponents_Overline)
                setTextColorAttr(android.R.attr.textColorPrimary)
            }.also(::addView)
        }.also(::addView)

        TextView(context).apply {
            layoutParams =
                LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT).apply {
                    topMargin = 12.dp()
                }
            text = "Directed by"
            setTextStyle(R.style.TextAppearance_MaterialComponents_Overline)
            setTextColorAttr(android.R.attr.textColorSecondary)
        }.also(::addView)

        TextView(context).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            text = "Rian Johnson"
            setTextStyle(R.style.TextAppearance_MaterialComponents_Body2)
            setTextColorAttr(android.R.attr.textColorPrimary)
        }.also(::addView)

        TextView(context).apply {
            layoutParams =
                LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT).apply {
                    topMargin = 12.dp()
                }
            text = "Written by"
            setTextStyle(R.style.TextAppearance_MaterialComponents_Overline)
            setTextColorAttr(android.R.attr.textColorSecondary)
        }.also(::addView)

        TextView(context).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            text = "Rian Johnson, George Lucas"
            setTextStyle(R.style.TextAppearance_MaterialComponents_Body2)
            setTextColorAttr(android.R.attr.textColorPrimary)
        }.also(::addView)

        TextView(context).apply {
            layoutParams =
                LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT).apply {
                    topMargin = 12.dp()
                }
            text = "Summary"
            setTextStyle(R.style.TextAppearance_MaterialComponents_Overline)
            setTextColorAttr(android.R.attr.textColorSecondary)
        }.also(::addView)

        TextView(context).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            text =
                "Having taken her first steps into the Jedi world, Rey joins Luke Skywalker on an adventure with Leia, Finn and Poe that unlocks mysteries of the Force and secrets of the past."
            setTextStyle(R.style.TextAppearance_MaterialComponents_Body1)
            setTextColorAttr(android.R.attr.textColorPrimary)
        }.also(::addView)
    }
}

class DetailScreenTransitionRoute : TransitionRoute {
    override fun transition(): Transition = TransitionSet().apply {
        Fade().setInterpolator(DecelerateInterpolator()).also { addTransition(it) }
    }
}