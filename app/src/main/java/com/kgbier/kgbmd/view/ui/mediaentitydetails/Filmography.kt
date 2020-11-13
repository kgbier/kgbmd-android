package com.kgbier.kgbmd.view.ui.mediaentitydetails

import android.content.Context
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import androidx.core.view.updatePaddingRelative
import com.kgbier.kgbmd.Navigator
import com.kgbier.kgbmd.Route
import com.kgbier.kgbmd.util.dp
import com.kgbier.kgbmd.view.ui.FilmographyView

class FilmographyViewModel(
    val title: String,
    val year: String?,
    val role: String?,
    val titleId: String?,
) : BaseMediaEntityListItemViewModel {
    fun showTitleDetails() {
        titleId?.let { Navigator.navigate(Route.DetailScreen(it)) }
    }
}

class FilmographyViewHolder(context: Context) :
    BaseMediaEntityListItemViewHolder(FilmographyView(context).apply {
        layoutParams = ViewGroup.MarginLayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        )
        updatePaddingRelative(start = 16.dp, top = 4.dp, end = 16.dp, bottom = 4.dp)
    }) {
    val view get() = itemView as FilmographyView

    override fun bind(viewModel: BaseMediaEntityListItemViewModel) {
        if (viewModel !is FilmographyViewModel) return

        view.setText(viewModel.title, viewModel.year, viewModel.role)

        if (viewModel.titleId == null) {
            view.isClickable = false
            view.showAction(false)
        } else {
            view.setOnClickListener { viewModel.showTitleDetails() }
            view.showAction(true)
        }
    }
}
