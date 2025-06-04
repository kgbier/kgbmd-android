package com.kgbier.kgbmd.view.component

import android.annotation.SuppressLint
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
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

    private val movieListSearchViewModel: MovieListSearchViewModel by context.viewModels()

    init {
        editTextSearch.hint = movieListSearchViewModel.hint
        editTextSearch.isEnabled = false
        editTextSearch.movementMethod = null
        editTextSearch.keyListener = null

        foreground = resolveAttribute(android.R.attr.selectableItemBackground)
            ?.let { ContextCompat.getDrawable(context, it) }
        stateListAnimator = ElevateCardViewStateListAnimator(context, 4f.dp, 8f.dp)

        setOnClickListener {
            context.navigate(Route.SearchScreen)
        }
    }

    override fun updateCornerRadius(layoutHeight: Int) {
        radius = layoutHeight / 2f
    }
}
