package com.kgbier.kgbmd

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import java.util.*

class MainActivity : AppCompatActivity(), CoroutineScope by MainScope(), LifecycleOwner {

    companion object {
        const val KEY_NAV_CURRENT_ROUTE = "kgbmd:NAV_CURRENT_ROUTE"
        const val KEY_NAV_BACK_STACK = "kgbmd:NAV_BACK_STACK"
        const val ROUTE_UNDEFINED = -1
    }

    private var currentRoute: Route? = null
    private val backStack = Stack<Route>()

    private val rootView: ViewGroup
        get() = window.decorView.findViewById(android.R.id.content)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        rootView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)

        val routeOrdinal =
            savedInstanceState?.getInt(KEY_NAV_CURRENT_ROUTE, ROUTE_UNDEFINED) ?: ROUTE_UNDEFINED

        savedInstanceState?.getIntArray(KEY_NAV_BACK_STACK)?.map {
            Route.getRoute(RouteId.values()[it])
        }?.let {
            backStack.addAll(it)
        }

        val route = if (routeOrdinal == ROUTE_UNDEFINED) {
            Route.MainPosterScreen
        } else {
            Route.getRoute(RouteId.values()[routeOrdinal])
        }
        showScreen(route)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(
            KEY_NAV_CURRENT_ROUTE,
            currentRoute?.id?.ordinal ?: ROUTE_UNDEFINED
        )
        outState.putIntArray(
            KEY_NAV_BACK_STACK,
            backStack.map { it.id.ordinal }.toIntArray()
        )
    }

    fun navigate(route: Route) {
        if (currentRoute !== null) backStack.push(currentRoute)
        showScreen(route)
    }

    fun navigateBack() {
        if (backStack.isEmpty()) {
            finish(); return
        }
        showScreen(backStack.pop())
    }

    private fun showScreen(route: Route) {
        currentRoute = route
        Navigation.routeTo(route, rootView, this)
    }

    override fun onBackPressed() = navigateBack()

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }
}