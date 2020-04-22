package com.kgbier.kgbmd

import android.os.Parcelable
import android.transition.Scene
import android.transition.Transition
import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup
import com.kgbier.kgbmd.view.*
import kotlinx.android.parcel.Parcelize

interface LayoutRoute {
    fun layout(context: MainActivity, route: Route): View
}

interface SceneRoute {
    fun scene(rootView: ViewGroup, layout: View): Scene
}

interface TransitionRoute {
    fun transition(): Transition
}

enum class RouteId {
    ROUTE_MAIN_POSTER_SCREEN,
    ROUTE_SEARCH_SCREEN,
    ROUTE_DETAIL_SCREEN
}

@Parcelize
class ErrorParcelable : Parcelable, Throwable()

@Parcelize
data class RouteParcel(val routeId: RouteId, val route: Parcelable) : Parcelable

fun Route.parcelled() = RouteParcel(id, this as? Parcelable ?: ErrorParcelable())

sealed class Route(val id: RouteId) : LayoutRoute {

    companion object {
        fun getRoute(route: RouteParcel): Route = when (route.routeId) {
            RouteId.ROUTE_MAIN_POSTER_SCREEN -> MainPosterScreen
            RouteId.ROUTE_SEARCH_SCREEN -> SearchScreen
            RouteId.ROUTE_DETAIL_SCREEN -> route.route as DetailScreen
        }

    }

    object MainPosterScreen : Route(RouteId.ROUTE_MAIN_POSTER_SCREEN),
        LayoutRoute by BaseLayoutRoute(::MainLayout),
        TransitionRoute by MainPosterScreenTransitionRoute()

    object SearchScreen : Route(RouteId.ROUTE_SEARCH_SCREEN),
        LayoutRoute by BaseLayoutRoute(::SearchLayout),
        SceneRoute by SearchScreenSceneRoute(),
        TransitionRoute by SearchScreenTransitionRoute()

    @Parcelize
    data class DetailScreen(val titleId: String) : Route(RouteId.ROUTE_DETAIL_SCREEN),
        LayoutRoute by BaseLayoutRoute(::DetailLayout),
        TransitionRoute by DetailScreenTransitionRoute(),
        Parcelable
}

class BaseLayoutRoute<T : View>(val provider: (MainActivity, Route) -> T) : LayoutRoute {
    override fun layout(context: MainActivity, route: Route): T = provider(context, route)
}

object Navigation {
    fun routeTo(route: Route, rootView: ViewGroup, context: MainActivity) {
        val newScene = if (route is SceneRoute) {
            route.scene(rootView, route.layout(context, route))
        } else {
            Scene(rootView, route.layout(context, route))
        }
        if (route is TransitionRoute) {
            TransitionManager.go(newScene, route.transition())
        } else {
            newScene.enter()
        }
    }
}
