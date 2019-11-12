package com.kgbier.kgbmd.view.ui

import android.annotation.SuppressLint
import android.text.InputType
import android.util.TypedValue
import android.view.Gravity
import android.widget.EditText
import androidx.cardview.widget.CardView
import androidx.core.view.setPadding
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProviders
import com.kgbier.kgbmd.MainActivity
import com.kgbier.kgbmd.util.dp
import com.kgbier.kgbmd.view.MovieListSearchViewModel

private const val HEIGHT = 40
private const val ELEVATION = 4f

@SuppressLint("ViewConstructor")
class SearchBarView(context: MainActivity) : CardView(context) {

    val editTextSearch: EditText

    private var movieListSearchViewModel: MovieListSearchViewModel =
        ViewModelProviders.of(context).get(MovieListSearchViewModel::class.java)

    init {
        minimumHeight = dp(HEIGHT)
        radius = minimumHeight / 2f
        cardElevation = dp(ELEVATION)

        editTextSearch = EditText(context).apply {
            inputType = InputType.TYPE_CLASS_TEXT
            background = null
            setPaddingRelative(0, dp(8), 0, dp(8))
            maxLines = 1
            hint = "Search movies, shows, actors"
            layoutParams =
                LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT,
                    Gravity.CENTER_VERTICAL
                ).apply {
                    marginStart = dp(8)
                    marginEnd = dp(8)
                }
            addTextChangedListener { text -> Unit }
        }.also(::addView)
    }
}
