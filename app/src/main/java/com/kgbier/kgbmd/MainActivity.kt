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

    private var currentRoute: Route? = null
    private val backStack = Stack<Route>()

    private val rootView: ViewGroup
        get() = window.decorView.findViewById(android.R.id.content)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        rootView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)

        navigate(Route.MainPosterScreen)
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