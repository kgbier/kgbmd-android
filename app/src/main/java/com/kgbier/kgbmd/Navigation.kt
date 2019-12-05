package com.kgbier.kgbmd

import android.transition.*
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import com.kgbier.kgbmd.view.MainLayout
import com.kgbier.kgbmd.view.SearchLayout
import com.kgbier.kgbmd.view.animation.CornerRadiusTransition

interface LayoutRoute {
    fun layout(context: MainActivity): View
}

class BaseLayoutRoute<T : View>(val provider: (MainActivity) -> T) : LayoutRoute {
    override fun layout(context: MainActivity): T = provider(context)
}

interface SceneRoute {
    fun scene(rootView: ViewGroup, layout: View): Scene
}

interface TransitionRoute {
    fun transition(): Transition
}

sealed class Route : LayoutRoute {
    object MainPosterScreen : Route(),
        LayoutRoute by BaseLayoutRoute(::MainLayout),
        TransitionRoute by MainPosterScreenTransitionRoute()

    object SearchScreen : Route(),
        LayoutRoute by BaseLayoutRoute(::SearchLayout),
        SceneRoute by SearchScreenSceneRoute(),
        TransitionRoute by SearchScreenTransitionRoute()
}

object Navigation {
    fun routeTo(route: Route, rootView: ViewGroup, context: MainActivity) {
        val newScene = if (route is SceneRoute) {
            route.scene(rootView, route.layout(context))
        } else {
            Scene(rootView, route.layout(context))
        }
        if (route is TransitionRoute) {
            TransitionManager.go(newScene, route.transition())
        } else {
            newScene.enter()
        }
    }
}

class SearchScreenSceneRoute : SceneRoute {
    override fun scene(rootView: ViewGroup, layout: View): Scene =
        Scene(rootView, layout).apply {
            if (layout is SearchLayout) {
                setEnterAction {
                    layout.onViewShown()
                }
            }
        }
}

class MainPosterScreenTransitionRoute : TransitionRoute {
    override fun transition(): Transition = TransitionSet().apply {
        ChangeBounds()
            .addTarget(R.id.searchBarView)
            .setInterpolator(DecelerateInterpolator())
            .also { addTransition(it) }

        CornerRadiusTransition()
            .addTarget(R.id.searchBarView)
            .setInterpolator(DecelerateInterpolator())
            .also { addTransition(it) }

        Slide()
            .addTarget(R.id.tilePosterView)
            .setInterpolator(DecelerateInterpolator()).also { addTransition(it) }

    }
}

class SearchScreenTransitionRoute : TransitionRoute {
    override fun transition(): Transition = TransitionSet().apply {
        ChangeBounds()
            .addTarget(R.id.searchBarView)
            .setInterpolator(DecelerateInterpolator())
            .also { addTransition(it) }

        CornerRadiusTransition()
            .addTarget(R.id.searchBarView)
            .setInterpolator(DecelerateInterpolator())
            .also { addTransition(it) }

        Slide()
            .addTarget(R.id.tilePosterView)
            .setInterpolator(AccelerateInterpolator()).also { addTransition(it) }
    }
}
