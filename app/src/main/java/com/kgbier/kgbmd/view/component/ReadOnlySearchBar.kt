package com.kgbier.kgbmd.view.component

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import com.kgbier.kgbmd.MainActivity
import com.kgbier.kgbmd.R
import com.kgbier.kgbmd.Route
import com.kgbier.kgbmd.util.dp
import com.kgbier.kgbmd.util.resolveAttribute
import com.kgbier.kgbmd.view.animation.ElevateCardViewStateListAnimator
import com.kgbier.kgbmd.view.ui.SearchBarView
import com.kgbier.kgbmd.view.viewmodel.MovieListSearchViewModel

@SuppressLint("ViewConstructor")
class ReadOnlySearchBar(context: MainActivity) : SearchBarView(context) {

    private var movieListSearchViewModel: MovieListSearchViewModel =
        ViewModelProvider(context)[MovieListSearchViewModel::class.java]

    init {
        editTextSearch.hint = movieListSearchViewModel.hint
        editTextSearch.isEnabled = false
        editTextSearch.movementMethod = null
        editTextSearch.keyListener = null

        foreground =
            resolveAttribute(R.attr.selectableItemBackground)?.let { context.getDrawable(it) }
        stateListAnimator = ElevateCardViewStateListAnimator(context, dp(4f), dp(8f))

        setOnClickListener {
            context.navigate(Route.SearchScreen)
        }
    }
}