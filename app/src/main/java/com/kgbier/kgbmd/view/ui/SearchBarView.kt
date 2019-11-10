package com.kgbier.kgbmd.view.ui

import android.content.Context
import androidx.cardview.widget.CardView
import com.kgbier.kgbmd.util.dp

private const val HEIGHT = 48
private const val ELEVATION = 4f

class SearchBarView(context: Context) : CardView(context) {

    init {
        minimumHeight = dp(HEIGHT)
        radius = minimumHeight / 2f
        cardElevation = dp(ELEVATION)
    }
}
