package com.kgbier.kgbmd.view

import android.annotation.SuppressLint
import android.widget.LinearLayout
import androidx.lifecycle.LifecycleOwner
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.kgbier.kgbmd.MainActivity
import com.kgbier.kgbmd.data.network.ImdbService
import com.kgbier.kgbmd.view.ui.TiledPosterView
import kotlinx.coroutines.launch

@SuppressLint("ViewConstructor")
class MainLayout(private val context: MainActivity) : LinearLayout(context),
    LifecycleOwner by context {

    private val swipeRefreshLayout: SwipeRefreshLayout
    private val tiledPosterView: TiledPosterView

    init {
//        val searchBarView = SearchBarView(context)
//
//        addView(
//            searchBarView,
//            MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT).apply {
//                marginStart = dp(16)
//                marginEnd = dp(16)
//                topMargin = dp(16)
//            })

        swipeRefreshLayout = SwipeRefreshLayout(context).apply {
            setOnRefreshListener { load() }
        }.also {
            addView(it, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
        }

        tiledPosterView = TiledPosterView(context)

        swipeRefreshLayout.addView(
            tiledPosterView,
            MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        )

        load()
    }

    private fun load() = context.launch {
        try {
            val movies = ImdbService.getHotMovies()
            tiledPosterView.setMovies(movies)
        } catch (t: Throwable) {
        } finally {
            swipeRefreshLayout.isRefreshing = false
        }
    }
}

