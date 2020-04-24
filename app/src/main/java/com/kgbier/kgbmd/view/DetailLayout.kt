package com.kgbier.kgbmd.view

import android.annotation.SuppressLint
import android.transition.Fade
import android.transition.Transition
import android.transition.TransitionSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.LinearLayout.HORIZONTAL
import android.widget.LinearLayout.VERTICAL
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.setMargins
import androidx.core.view.updatePadding
import androidx.core.view.updatePaddingRelative
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.kgbier.kgbmd.*
import com.kgbier.kgbmd.domain.model.MovieDetails
import com.kgbier.kgbmd.util.*
import com.kgbier.kgbmd.view.viewmodel.TitleDetailsViewModel

@SuppressLint("ViewConstructor")
class DetailLayout(context: MainActivity) :
    RouteReceiver<Route.DetailScreen> by Navigation.routeReceiver(),
    FrameLayout(context) {

    private val disposeBag = LiveDataDisposeBag()

    private val titleDetailsViewModel: TitleDetailsViewModel by context.viewModels(route.titleId) {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T =
                TitleDetailsViewModel(route.titleId) as T
        }
    }

    val layoutLoading: FrameLayout
    val layoutDetails: LinearLayout

    val textViewTitle: TextView
    val textViewReleaseDate: TextView
    val textViewContentRating: TextView
    val textViewDuration: TextView
    val textViewDirectedBy: TextView
    val textViewWrittenBy: TextView
    val textViewSummary: TextView

    init {
        updatePaddingRelative(start = 16.dp(), end = 16.dp(), top = 16.dp(), bottom = 16.dp())

        setOnUpdateWithWindowInsetsListener { _, insets, intendedPadding, _ ->
            updatePadding(
                top = intendedPadding.top + insets.systemWindowInsetTop,
                bottom = intendedPadding.bottom + insets.systemWindowInsetBottom
            )
            insets.consumeSystemWindowInsets()
        }

        layoutLoading = FrameLayout(context).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            visibility = View.VISIBLE

            ProgressBar(context).apply {
                layoutParams = LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT,
                    Gravity.CENTER
                ).apply {
                    setMargins(16.dp())
                }
            }.also(::addView)
        }.also(::addView)

        layoutDetails = LinearLayout(context).apply {
            orientation = VERTICAL
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            visibility = View.GONE

            textViewTitle = TextView(context).apply {
                layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
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
                    setTextStyle(R.style.TextAppearance_MaterialComponents_Overline)
                    setTextColorAttr(android.R.attr.textColorPrimary)
                }.also(::addView)

                textViewContentRating = TextView(context).apply {
                    layoutParams =
                        LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
                            marginEnd = 8.dp()
                        }
                    setTextStyle(R.style.TextAppearance_MaterialComponents_Overline)
                    setTextColorAttr(android.R.attr.textColorPrimary)
                }.also(::addView)

                textViewDuration = TextView(context).apply {
                    layoutParams =
                        LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
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
                setTextStyle(R.style.TextAppearance_MaterialComponents_Body1)
                setTextColorAttr(android.R.attr.textColorPrimary)
            }.also(::addView)
        }.also(::addView)

        titleDetailsViewModel.titleDetails.bind(context) {
            when (it) {
                is TitleDetailsViewModel.TitleDetailsState.Loaded -> showDetails(it.details)
                is TitleDetailsViewModel.TitleDetailsState.Error -> showError(it.message)
                TitleDetailsViewModel.TitleDetailsState.Loading -> showLoading()
            }
        }.disposeBy(disposeBag)
    }

    fun showDetails(details: MovieDetails) = with(details) {
        layoutLoading.visibility = View.GONE
        layoutDetails.visibility = View.VISIBLE

        textViewTitle.text = name
        textViewReleaseDate.text = yearReleased
        textViewContentRating.text = contentRating
        textViewDuration.text = duration
        textViewDirectedBy.text = directedBy
        textViewWrittenBy.text = writtenBy
        textViewSummary.text = description
    }

    fun showError(message: String) {
        layoutLoading.visibility = View.GONE
        Snackbar.make(this, message, Snackbar.LENGTH_SHORT).show()
    }

    fun showLoading() {
        layoutLoading.visibility = View.VISIBLE
        layoutDetails.visibility = View.GONE
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
