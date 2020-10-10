package com.kgbier.kgbmd

import android.os.Parcelable
import android.view.View
import android.view.ViewGroup
import androidx.transition.Scene
import androidx.transition.Transition
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import com.kgbier.kgbmd.view.*
import kotlinx.android.parcel.Parcelize
import java.lang.ref.WeakReference

interface RouteEventObserver {
    fun onExit()
}

interface RouteReceiver<R : Route> {
    val route: R
}

interface LayoutRoute {
    fun layout(context: MainActivity): View
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
        ROUTE_DETAIL_SCREEN,
        ROUTE_PHOTO_SCREEN
    }

    companion object {
        fun getRoute(route: RouteParcel): Route = when (route.routeId) {
            Id.ROUTE_MAIN_POSTER_SCREEN -> MainPosterScreen
            Id.ROUTE_SEARCH_SCREEN -> SearchScreen
            Id.ROUTE_DETAIL_SCREEN -> route.route as DetailScreen
            Id.ROUTE_PHOTO_SCREEN -> route.route as PhotoScreen
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

    @Parcelize
    data class PhotoScreen(val fullImage: String) : Route(Id.ROUTE_PHOTO_SCREEN),
        LayoutRoute by BaseLayoutRoute(::PhotoLayout),
        TransitionRoute by PhotoScreenTransitionRoute(),
        Parcelable
}

class BaseLayoutRoute<T : View>(val provider: (MainActivity) -> T) : LayoutRoute {
    override fun layout(context: MainActivity): T = provider(context)
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

    private val observers: MutableMap<String, WeakReference<RouteEventObserver>> = mutableMapOf()

    fun routeTo(route: Route, oldRoute: Route?, rootView: ViewGroup, context: MainActivity) {
        latchedRoute = route
        val layout = route.layout(context)
        if (layout is RouteEventObserver) {
            observers[route.toString()] = WeakReference<RouteEventObserver>(layout)
        }
        val newScene = if (route is SceneRoute) {
            route.scene(rootView, layout)
        } else {
            Scene(rootView, layout)
        }
        if (route is TransitionRoute || oldRoute is TransitionRoute) {
            val ensemble = TransitionSet().apply {
                if (oldRoute is TransitionRoute) {
                    addTransition(oldRoute.transition())
                }
                if (route is TransitionRoute) {
                    addTransition(route.transition())
                }
            }

            TransitionManager.go(newScene, ensemble)
        } else {
            newScene.enter()
        }
    }

    fun exit(route: Route) {
        val routeKey = route.toString()
        observers[routeKey]?.get()?.onExit()
        observers.remove(routeKey)
    }
}

object Navigator {
    var mainActivity: MainActivity? = null

    fun navigate(route: Route) = mainActivity?.navigate(route)

    fun navigateBack() = mainActivity?.navigateBack()
}
