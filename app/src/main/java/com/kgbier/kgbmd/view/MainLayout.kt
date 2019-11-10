package com.kgbier.kgbmd.view

import android.annotation.SuppressLint
import android.widget.LinearLayout
import androidx.lifecycle.LifecycleOwner
import com.kgbier.kgbmd.MainActivity
import com.kgbier.kgbmd.data.network.ImdbService
import com.kgbier.kgbmd.view.ui.TiledPosterView
import kotlinx.coroutines.launch


@SuppressLint("ViewConstructor")
class MainLayout(context: MainActivity) : LinearLayout(context),
    LifecycleOwner by context {

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

        val tiledPosterView = TiledPosterView(context)

        addView(
            tiledPosterView,
            MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        )

        context.launch {
            tiledPosterView.setMovies(ImdbService.getHotMovies())
        }
    }
}

