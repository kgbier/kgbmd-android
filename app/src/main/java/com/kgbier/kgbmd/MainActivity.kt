package com.kgbier.kgbmd

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import com.kgbier.kgbmd.view.MainLayout
import com.kgbier.kgbmd.view.SearchLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

sealed class Navigator {
    object MainPosterScreen : Navigator()
    object SearchScreen : Navigator()
}

class MainActivity : AppCompatActivity(), CoroutineScope by MainScope(), LifecycleOwner {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showScreen(Navigator.MainPosterScreen)
    }

    fun showScreen(navigator: Navigator) = when (navigator) {
        Navigator.MainPosterScreen -> setContentView(MainLayout(this))
        Navigator.SearchScreen -> setContentView(SearchLayout(this))
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }
}