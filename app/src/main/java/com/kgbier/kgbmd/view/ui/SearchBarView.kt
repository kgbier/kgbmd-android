package com.kgbier.kgbmd.view.ui

import android.annotation.SuppressLint
import android.text.InputType
import android.widget.EditText
import androidx.cardview.widget.CardView
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProviders
import com.kgbier.kgbmd.MainActivity
import com.kgbier.kgbmd.util.dp
import com.kgbier.kgbmd.view.MovieListSearchViewModel

private const val HEIGHT = 48
private const val ELEVATION = 4f

@SuppressLint("ViewConstructor")
class SearchBarView(context: MainActivity) : CardView(context) {

    private var movieListSearchViewModel: MovieListSearchViewModel =
        ViewModelProviders.of(context).get(MovieListSearchViewModel::class.java)

    init {
        minimumHeight = dp(HEIGHT)
        radius = minimumHeight / 2f
        cardElevation = dp(ELEVATION)

        val editTextSearch = EditText(context).apply {
            inputType = InputType.TYPE_CLASS_TEXT
            maxLines = 1
            layoutParams =
                MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT).apply {
                    marginStart = dp(8)
                    marginEnd = dp(8)
                }
            addTextChangedListener { text -> Unit }
        }.also(::addView)
    }
}
