package com.kgbier.kgbmd.view

import android.annotation.SuppressLint
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.TextAppearanceSpan
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
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.toSpannable
import androidx.core.view.setMargins
import androidx.core.view.updatePadding
import androidx.core.view.updatePaddingRelative
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.kgbier.kgbmd.*
import com.kgbier.kgbmd.domain.model.TitleDetails
import com.kgbier.kgbmd.util.*
import com.kgbier.kgbmd.view.ui.HeroRatingView
import com.kgbier.kgbmd.view.ui.TitledTextView
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
    val textViewDuration: TextView
    val viewDirectedBy: TitledTextView
    val viewWrittenBy: TitledTextView
    val viewCreatedBy: TitledTextView
    val viewSummary: TitledTextView
    val viewHeroRating: HeroRatingView

    init {
        updatePaddingRelative(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 16.dp)

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
                    setMargins(16.dp)
                }
            }.also(::addView)
        }.also(::addView)

        layoutDetails = LinearLayout(context).apply {
            orientation = VERTICAL
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            visibility = View.GONE

            textViewTitle = TextView(context).apply {
                layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
                setTextStyleAttr(R.attr.textAppearanceHeadline5)
                setTextColorAttr(android.R.attr.textColorPrimary)
            }.also(::addView)

            LinearLayout(context).apply {
                orientation = HORIZONTAL
                layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
                    topMargin = 12.dp
                }

                textViewDuration = TextView(context).apply {
                    layoutParams =
                        LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
                    setTextStyleAttr(R.attr.textAppearanceOverline)
                    setTextColorAttr(android.R.attr.textColorPrimary)
                }.also(::addView)
            }.also(::addView)

            viewHeroRating = HeroRatingView(context).apply {
                layoutParams =
                    LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
                        topMargin = 12.dp
                        gravity = Gravity.END
                    }
            }.also(::addView)

            viewDirectedBy = TitledTextView(context).apply {
                layoutParams =
                    LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT).apply {
                        topMargin = 12.dp
                    }
                textViewTitle.text = "Directed by"
            }.also(::addView)

            viewWrittenBy = TitledTextView(context).apply {
                layoutParams =
                    LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT).apply {
                        topMargin = 12.dp
                    }
                textViewTitle.text = "Written by"
            }.also(::addView)

            viewCreatedBy = TitledTextView(context).apply {
                layoutParams =
                    LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT).apply {
                        topMargin = 12.dp
                    }
                textViewTitle.text = "Created by"
            }.also(::addView)

            viewSummary = TitledTextView(context).apply {
                layoutParams =
                    LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT).apply {
                        topMargin = 12.dp
                    }
                textViewTitle.text = "Summary"
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

    fun showDetails(details: TitleDetails) = with(details) {
        layoutLoading.visibility = View.GONE
        layoutDetails.visibility = View.VISIBLE

        val titleSequence = SpannableStringBuilder().apply {
            append(name)
            yearReleased?.let { yearReleased ->
                val subtitleStyleSpan =
                    TextAppearanceSpan(context, R.style.TextAppearance_MaterialComponents_Subtitle1)
                val secondaryColorSpan = resolveAttribute(android.R.attr.textColorSecondary)?.let {
                    ResourcesCompat.getColor(resources, it, context.theme)
                }?.let {
                    ForegroundColorSpan(it)
                }

                append("\u00A0") // non-breaking space
                setSpan(subtitleStyleSpan, length, length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                secondaryColorSpan?.let {
                    setSpan(it, length, length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                }
                append("($yearReleased)")
            }
        }.toSpannable()

        textViewTitle.text = titleSequence
        textViewDuration.text = duration
        directedBy?.let {
            viewDirectedBy.visibility = View.VISIBLE
            viewDirectedBy.text = it
        } ?: run {
            viewDirectedBy.visibility = View.GONE
        }
        writtenBy?.let {
            viewWrittenBy.visibility = View.VISIBLE
            viewWrittenBy.text = it
        } ?: run {
            viewWrittenBy.visibility = View.GONE
        }
        createdBy?.let {
            viewCreatedBy.visibility = View.VISIBLE
            viewCreatedBy.text = it
        } ?: run {
            viewCreatedBy.visibility = View.GONE
        }
        description?.let {
            viewSummary.visibility = View.VISIBLE
            viewSummary.text = it
        } ?: run {
            viewSummary.visibility = View.GONE
        }

        details.rating?.let {
            viewHeroRating.visibility = View.VISIBLE
            viewHeroRating.setRating(it.value, it.best, it.count)
        } ?: run {
            viewHeroRating.visibility = View.GONE
        }
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
