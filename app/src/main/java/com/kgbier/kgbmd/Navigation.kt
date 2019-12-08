package com.kgbier.kgbmd

import android.transition.Scene
import android.transition.Transition
import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup
import com.kgbier.kgbmd.view.*

interface LayoutRoute {
    fun layout(context: MainActivity): View
}

interface SceneRoute {
    fun scene(rootView: ViewGroup, layout: View): Scene
}

interface TransitionRoute {
    fun transition(): Transition
}

enum class RouteId {
    ROUTE_MAIN_POSTER_SCREEN,
    ROUTE_SEARCH_SCREEN
}

sealed class Route(val id: RouteId) : LayoutRoute {

    companion object {
        fun getRoute(routeId: RouteId): Route = when (routeId) {
            RouteId.ROUTE_MAIN_POSTER_SCREEN -> MainPosterScreen
            RouteId.ROUTE_SEARCH_SCREEN -> SearchScreen
        }
    }

    object MainPosterScreen : Route(RouteId.ROUTE_MAIN_POSTER_SCREEN),
        LayoutRoute by BaseLayoutRoute(::MainLayout),
        TransitionRoute by MainPosterScreenTransitionRoute()

    object SearchScreen : Route(RouteId.ROUTE_SEARCH_SCREEN),
        LayoutRoute by BaseLayoutRoute(::SearchLayout),
        SceneRoute by SearchScreenSceneRoute(),
        TransitionRoute by SearchScreenTransitionRoute()
}

class BaseLayoutRoute<T : View>(val provider: (MainActivity) -> T) : LayoutRoute {
    override fun layout(context: MainActivity): T = provider(context)
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
