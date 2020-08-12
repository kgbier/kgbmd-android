package com.kgbier.kgbmd.view

import android.annotation.SuppressLint
import android.view.Gravity
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ProgressBar
import androidx.appcompat.view.ContextThemeWrapper
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.setMargins
import androidx.core.view.updatePadding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.transition.Fade
import androidx.transition.Slide
import androidx.transition.Transition
import androidx.transition.TransitionSet
import com.google.android.material.snackbar.Snackbar
import com.kgbier.kgbmd.*
import com.kgbier.kgbmd.util.*
import com.kgbier.kgbmd.view.behaviour.ScrollBehaviour
import com.kgbier.kgbmd.view.component.TitleDetailsList
import com.kgbier.kgbmd.view.ui.ToolbarView
import com.kgbier.kgbmd.view.viewmodel.TitleDetailsViewModel

@SuppressLint("ViewConstructor")
class DetailLayout(context: MainActivity) :
    RouteReceiver<Route.DetailScreen> by Navigation.routeReceiver(),
    CoordinatorLayout(context) {

    private val disposeBag = LiveDataDisposeBag()

    private val titleDetailsViewModel: TitleDetailsViewModel by context.viewModels(route.titleId) {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T =
                TitleDetailsViewModel(route.titleId) as T
        }
    }

    val toolbar: ToolbarView
    val progressBar: ProgressBar
    val titleDetailsList: TitleDetailsList

    init {
        id = R.id.detailLayout

        resolveColorAttribute(android.R.attr.colorBackground)?.let {
            setBackgroundColor(it)
        }

        titleDetailsList = TitleDetailsList(context, route.titleId).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            visibility = View.GONE
            clipToPadding = false

            setOnUpdateWithWindowInsetsListener { _, insets, intendedPadding, _ ->
                updatePadding(
                    bottom = intendedPadding.bottom + insets.systemWindowInsetBottom
                )
                insets.consumeSystemWindowInsets()
            }
        }.also(::addView)

        toolbar = ToolbarView(
            ContextThemeWrapper(context, R.style.Theme_MaterialComponents)
        ).apply {
            layoutParams =
                LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT).apply {
                    behavior = ScrollBehaviour<ToolbarView>()
                }

            setOnUpdateWithWindowInsetsListener { _, insets, _, _ ->
                updatePadding(top = insets.systemWindowInsetTop)
                insets.consumeSystemWindowInsets()
            }

            setNavigationIcon(R.drawable.ic_close)
            setNavigationOnClickListener { context.navigateBack() }
        }.also(::addView)

        progressBar = ProgressBar(context).apply {
            layoutParams = LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.CENTER
                setMargins(16.dp)
            }
            visibility = View.VISIBLE
        }.also(::addView)

//c

        titleDetailsViewModel.titleDetails.bind(context) {
            when (it) {
                is TitleDetailsViewModel.TitleDetailsState.Loaded -> showDetails()
                is TitleDetailsViewModel.TitleDetailsState.Error -> showError(it.message)
                TitleDetailsViewModel.TitleDetailsState.Loading -> showLoading()
            }
        }.disposeBy(disposeBag)
    }

    fun showDetails() {
        progressBar.visibility = View.GONE
        titleDetailsList.visibility = View.VISIBLE
    }

    fun showError(message: String) {
        progressBar.visibility = View.GONE
        Snackbar.make(this, message, Snackbar.LENGTH_SHORT).show()
    }

    fun showLoading() {
        progressBar.visibility = View.VISIBLE
        titleDetailsList.visibility = View.GONE
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
