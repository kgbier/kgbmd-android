package com.kgbier.kgbmd

import android.os.Parcelable
import android.transition.Scene
import android.transition.Transition
import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup
import com.kgbier.kgbmd.view.*
import kotlinx.android.parcel.Parcelize

interface RouteReceiver<R : Route> {
    val route: R
}

interface LayoutRoute {
    fun layout(context: MainActivity, route: Route): View
}

interface SceneRoute {
    fun scene(rootView: ViewGroup, layout: View): Scene
}

interface TransitionRoute {
    fun transition(): Transition
}

@Parcelize
class ErrorParcelable : Parcelable, Throwable()

@Parcelize
data class RouteParcel(val routeId: Route.Id, val route: Parcelable) : Parcelable

fun Route.parcelled() = RouteParcel(id, this as? Parcelable ?: ErrorParcelable())

sealed class Route(val id: Id) : LayoutRoute {

    enum class Id {
        ROUTE_MAIN_POSTER_SCREEN,
        ROUTE_SEARCH_SCREEN,
        ROUTE_DETAIL_SCREEN
    }

    companion object {
        fun getRoute(route: RouteParcel): Route = when (route.routeId) {
            Id.ROUTE_MAIN_POSTER_SCREEN -> MainPosterScreen
            Id.ROUTE_SEARCH_SCREEN -> SearchScreen
            Id.ROUTE_DETAIL_SCREEN -> route.route as DetailScreen
        }

    }

    object MainPosterScreen : Route(Id.ROUTE_MAIN_POSTER_SCREEN),
        LayoutRoute by BaseLayoutRoute(::MainLayout),
        TransitionRoute by MainPosterScreenTransitionRoute()

    object SearchScreen : Route(Id.ROUTE_SEARCH_SCREEN),
        LayoutRoute by BaseLayoutRoute(::SearchLayout),
        SceneRoute by SearchScreenSceneRoute(),
        TransitionRoute by SearchScreenTransitionRoute()

    @Parcelize
    data class DetailScreen(val titleId: String) : Route(Id.ROUTE_DETAIL_SCREEN),
        LayoutRoute by BaseLayoutRoute(::DetailLayout),
        TransitionRoute by DetailScreenTransitionRoute(),
        Parcelable
}

class BaseLayoutRoute<T : View>(val provider: (MainActivity) -> T) : LayoutRoute {
    override fun layout(context: MainActivity, route: Route): T = provider(context)
}

object Navigation {

    private var latchedRoute: Route? = null
    fun <R : Route> routeReceiver(): RouteReceiver<R> {
        val receiver = object : RouteReceiver<R> {
            @Suppress("UNCHECKED_CAST")
            override val route: R = latchedRoute!! as R
        }
        latchedRoute = null
        return receiver
    }

    fun <R : Route> routeTo(route: R, rootView: ViewGroup, context: MainActivity) {
        latchedRoute = route
        val layout = route.layout(context, route)
        val newScene = if (route is SceneRoute) {
            route.scene(rootView, layout)
        } else {
            Scene(rootView, layout)
        }
        if (route is TransitionRoute) {
            TransitionManager.go(newScene, route.transition())
        } else {
            newScene.enter()
        }
    }
}
