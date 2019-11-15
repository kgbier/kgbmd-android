package com.kgbier.kgbmd.view.ui

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.ConstraintSet.*
import com.kgbier.kgbmd.util.dp

class SearchSuggestionView(context: Context) : ConstraintLayout(context) {

    val imageViewThumbnail: ImageView
    val textViewTitle: TextView
    val textViewTidbit: TextView
    val textViewYear: TextView

    init {

        imageViewThumbnail = ImageView(context).apply {
            id = View.generateViewId()
        }.also(::addView)

        ConstraintSet().apply {
            constrainWidth(imageViewThumbnail.id, WRAP_CONTENT)
            constrainHeight(imageViewThumbnail.id, WRAP_CONTENT)
            connect(
                imageViewThumbnail.id,
                START,
                PARENT_ID,
                START
            )
            connect(
                imageViewThumbnail.id,
                TOP,
                PARENT_ID,
                TOP
            )
        }.applyTo(this)

        textViewTitle = TextView(context).apply {
            id = View.generateViewId()

            textSize = 16f
        }.also(::addView)

        ConstraintSet().apply {
            constrainWidth(textViewTitle.id, WRAP_CONTENT)
            constrainHeight(textViewTitle.id, WRAP_CONTENT)
            connect(
                textViewTitle.id,
                START,
                imageViewThumbnail.id,
                START
            )
            connect(
                textViewTitle.id,
                TOP,
                imageViewThumbnail.id,
                TOP
            )
        }.applyTo(this)

        textViewTidbit = TextView(context).apply {
            id = View.generateViewId()

            textSize = 14f
        }.also(::addView)

        ConstraintSet().apply {
            constrainWidth(textViewTidbit.id, WRAP_CONTENT)
            constrainHeight(textViewTidbit.id, WRAP_CONTENT)
            connect(
                textViewTidbit.id,
                START,
                textViewTitle.id,
                START
            )
            connect(
                textViewTidbit.id,
                TOP,
                textViewTitle.id,
                BOTTOM
            )
            connect(
                textViewTidbit.id,
                BOTTOM,
                PARENT_ID,
                BOTTOM
            )
        }.applyTo(this)

        textViewYear = TextView(context).apply {
            id = View.generateViewId()

            textSize = 12f
        }.also(::addView)

        ConstraintSet().apply {
            constrainWidth(textViewYear.id, WRAP_CONTENT)
            constrainHeight(textViewYear.id, WRAP_CONTENT)
            setMargin(textViewYear.id, START, dp(4))
            connect(
                textViewYear.id,
                START,
                textViewTitle.id,
                END
            )
            connect(
                textViewYear.id,
                BASELINE,
                textViewTitle.id,
                BASELINE
            )
        }.applyTo(this)
    }

    fun setThumbnail(url: String) {

    }

    fun setTitle(title: String) {
        textViewTitle.text = title
    }

    fun setYear(year: String?) {
        textViewYear.text = "($year)"
    }

    fun setTidbit(tidbit: String?) {
        textViewTidbit.text = tidbit
    }
}