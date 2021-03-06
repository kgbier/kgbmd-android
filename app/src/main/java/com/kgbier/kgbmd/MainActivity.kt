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
    }

    private var currentRoute: Route? = null
    private val backStack = Stack<Route>()

    /**
     * So there's this real weird bug. Initially, I had an assumption that fetching this view by id would always succeed.
     * However, this has since been proven false (on at least one emualator and one real device).
     *
     * When navigating SPECIFICALLY to the "PhotoScreen", if the system needs to recreate the screen for any reason
     * (theme is changed, screen is rotated, etc.) once onCreate occurs, and the navigation hierarchy is restored,
     * the root ContentFrameLayout is nowhere to be seen. Pressing the system "back" button will
     * try to show the next screen, attempt to find the ContentFrameLayout by id, and will fail since
     * it is not present in the view hierarchy.
     *
     * Setting this root view to lazy captures it and keeps everything working for some crazy reason.
     */
    private val rootView: ViewGroup by lazy {
        window.decorView.findViewById(android.R.id.content)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Navigator.mainActivity = this

        rootView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)

        savedInstanceState?.getParcelableArray(KEY_NAV_BACK_STACK)?.map {
            Route.getRoute(it as RouteParcel)
        }?.let {
            backStack.addAll(it)
        }

        val route = savedInstanceState?.getParcelable<RouteParcel>(KEY_NAV_CURRENT_ROUTE)
            ?.let {
                Route.getRoute(it)
            } ?: Route.MainPosterScreen

        showScreen(route)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArray(
            KEY_NAV_BACK_STACK,
            backStack.map { it.parcelled() }.toTypedArray()
        )
        outState.putParcelable(
            KEY_NAV_CURRENT_ROUTE,
            currentRoute?.parcelled()
        )
    }

    fun navigate(route: Route) {
        if (currentRoute !== null) backStack.push(currentRoute)
        showScreen(route)
    }

    fun navigateBack() {
        currentRoute?.let { Navigation.exit(it) }
        if (backStack.isEmpty()) {
            finish()
        } else {
            showScreen(backStack.pop())
        }
    }

    private fun showScreen(route: Route) {
        Navigation.routeTo(route, currentRoute, rootView, this)
        currentRoute = route
    }

    override fun onBackPressed() = navigateBack()

    override fun onDestroy() {
        super.onDestroy()
        Navigator.mainActivity = null
        cancel()
    }
}
