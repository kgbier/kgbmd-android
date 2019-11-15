package com.kgbier.kgbmd

import android.os.Bundle
import android.transition.Scene
import android.transition.Slide
import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import com.kgbier.kgbmd.view.MainLayout
import com.kgbier.kgbmd.view.SearchLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import java.util.*

sealed class Route {
    object MainPosterScreen : Route()
    object SearchScreen : Route()
}

class MainActivity : AppCompatActivity(), CoroutineScope by MainScope(), LifecycleOwner {

    private var currentRoute: Route? = null
    private val backStack = Stack<Route>()

    private val rootView: ViewGroup
        get() = window.decorView.findViewById(android.R.id.content)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigate(Route.MainPosterScreen)
    }

    fun navigate(route: Route) {
        if (currentRoute !== null) backStack.push(currentRoute)
        showScreen(route)
    }

    private fun showScreen(route: Route) {
        currentRoute = route
        when (route) {
            Route.MainPosterScreen -> {
                val newScene = Scene(rootView, MainLayout(this) as View)
                val transition =
                    Slide().addTarget(R.id.tilePosterView).setInterpolator(DecelerateInterpolator())
                TransitionManager.go(newScene, transition)
            }
            Route.SearchScreen -> {
                val newScene = Scene(rootView, SearchLayout(this) as View)
                val transition =
                    Slide().addTarget(R.id.tilePosterView).setInterpolator(AccelerateInterpolator())
                TransitionManager.go(newScene, transition)
            }
        }
    }

    override fun onBackPressed() {
        if (backStack.isEmpty()) {
            finish(); return
        }
        showScreen(backStack.pop())
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }
}