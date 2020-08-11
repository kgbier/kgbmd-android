package com.kgbier.kgbmd.view

import android.annotation.SuppressLint
import android.view.Gravity
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.core.view.setMargins
import androidx.core.view.updateMarginsRelative
import androidx.core.view.updatePadding
import androidx.core.view.updatePaddingRelative
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.transition.Fade
import androidx.transition.Slide
import androidx.transition.Transition
import androidx.transition.TransitionSet
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.Snackbar
import com.kgbier.kgbmd.*
import com.kgbier.kgbmd.util.*
import com.kgbier.kgbmd.view.component.ListingDetailsList
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

    val layoutLoading: FrameLayout
    val listingDetailsList: ListingDetailsList

    init {
        id = R.id.detailLayout

        orientation = VERTICAL

        MaterialToolbar(context).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

            setOnUpdateWithWindowInsetsListener { _, insets, _, _ ->
                (this.layoutParams as MarginLayoutParams).updateMarginsRelative(top = insets.systemWindowInsetTop)
                insets.consumeSystemWindowInsets()
            }

            setNavigationIcon(R.drawable.ic_close)
            setNavigationOnClickListener { context.navigateBack() }
        }.also(::addView)

        layoutLoading = FrameLayout(context).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, 0, 1f)
            visibility = View.VISIBLE

            ProgressBar(context).apply {
                layoutParams = FrameLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT,
                    Gravity.CENTER
                ).apply {
                    setMargins(16.dp)
                }
            }.also(::addView)
        }.also(::addView)

        listingDetailsList = ListingDetailsList(context, route.titleId).apply {
            setOnUpdateWithWindowInsetsListener { _, insets, intendedPadding, _ ->
                updatePadding(
                    bottom = intendedPadding.bottom + insets.systemWindowInsetBottom
                )
                insets.consumeSystemWindowInsets()
            }

            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, 0, 1f)
            visibility = View.GONE
        }.also(::addView)

        titleDetailsViewModel.titleDetails.bind(context) {
            when (it) {
                is TitleDetailsViewModel.TitleDetailsState.Loaded -> showDetails()
                is TitleDetailsViewModel.TitleDetailsState.Error -> showError(it.message)
                TitleDetailsViewModel.TitleDetailsState.Loading -> showLoading()
            }
        }.disposeBy(disposeBag)
    }

    fun showDetails() {
        layoutLoading.visibility = View.GONE
        listingDetailsList.visibility = View.VISIBLE
    }

    fun showError(message: String) {
        layoutLoading.visibility = View.GONE
        Snackbar.make(this, message, Snackbar.LENGTH_SHORT).show()
    }

    fun showLoading() {
        layoutLoading.visibility = View.VISIBLE
        listingDetailsList.visibility = View.GONE
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        disposeBag.dispose()
    }
}

class DetailScreenTransitionRoute : TransitionRoute {
    override fun transition(): Transition = TransitionSet().apply {
        Fade()
            .setInterpolator(DecelerateInterpolator())
            .also { addTransition(it) }

        Slide()
            .addTarget(R.id.detailLayout)
            .setInterpolator(DecelerateInterpolator())
            .also { addTransition(it) }
    }
}
