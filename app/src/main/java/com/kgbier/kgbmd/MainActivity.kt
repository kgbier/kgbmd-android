package com.kgbier.kgbmd

import android.os.Bundle
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
            Route.MainPosterScreen -> setContentView(MainLayout(this))
            Route.SearchScreen -> setContentView(SearchLayout(this))
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