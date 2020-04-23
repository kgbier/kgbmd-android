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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kgbier.kgbmd.*
import com.kgbier.kgbmd.domain.model.MovieDetails
import com.kgbier.kgbmd.util.*
import com.kgbier.kgbmd.view.viewmodel.TitleDetailsViewModel

@SuppressLint("ViewConstructor")
class DetailLayout(context: MainActivity) :
    RouteReceiver<Route.DetailScreen> by Navigation.routeReceiver(),
    LinearLayout(context) {

    private val disposeBag = LiveDataDisposeBag()

    private val titleDetailsViewModel: TitleDetailsViewModel by context.viewModels(route.titleId) {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T =
                TitleDetailsViewModel(route.titleId) as T
        }
    }

    val textViewTitle: TextView
    val textViewReleaseDate: TextView
    val textViewContentRating: TextView
    val textViewDuration: TextView
    val textViewDirectedBy: TextView
    val textViewWrittenBy: TextView
    val textViewSummary: TextView

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

        textViewTitle = TextView(context).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            text = "Loading"
            setTextStyle(R.style.TextAppearance_MaterialComponents_Headline5)
            setTextColorAttr(android.R.attr.textColorPrimary)
        }.also(::addView)

        LinearLayout(context).apply {
            orientation = HORIZONTAL
            layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
                topMargin = 12.dp()
            }

            textViewReleaseDate = TextView(context).apply {
                layoutParams =
                    LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
                        marginEnd = 8.dp()
                    }
                text = "2017"
                setTextStyle(R.style.TextAppearance_MaterialComponents_Overline)
                setTextColorAttr(android.R.attr.textColorPrimary)
            }.also(::addView)

            textViewContentRating = TextView(context).apply {
                layoutParams =
                    LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
                        marginEnd = 8.dp()
                    }
                text = "M"
                setTextStyle(R.style.TextAppearance_MaterialComponents_Overline)
                setTextColorAttr(android.R.attr.textColorPrimary)
            }.also(::addView)

            textViewDuration = TextView(context).apply {
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

        textViewDirectedBy = TextView(context).apply {
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

        textViewWrittenBy = TextView(context).apply {
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

        textViewSummary = TextView(context).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            text =
                "Having taken her first steps into the Jedi world, Rey joins Luke Skywalker on an adventure with Leia, Finn and Poe that unlocks mysteries of the Force and secrets of the past."
            setTextStyle(R.style.TextAppearance_MaterialComponents_Body1)
            setTextColorAttr(android.R.attr.textColorPrimary)
        }.also(::addView)

        titleDetailsViewModel.titleDetails.bind(context) {
            when (it) {
                is TitleDetailsViewModel.TitleDetailsState.Loaded -> showDetails(it.details)
                else -> Unit
            }
        }.disposeBy(disposeBag)
    }

    fun showDetails(details: MovieDetails) = with(details) {
        textViewTitle.text = name
        textViewReleaseDate.text = yearReleased
        textViewDuration.text = duration
        textViewSummary.text = description
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        disposeBag.dispose()
    }
}

class DetailScreenTransitionRoute : TransitionRoute {
    override fun transition(): Transition = TransitionSet().apply {
        Fade().setInterpolator(DecelerateInterpolator()).also { addTransition(it) }
    }
}