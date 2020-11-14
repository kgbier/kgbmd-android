package com.kgbier.kgbmd.view.ui.mediaentitydetails

import android.content.Context
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import androidx.core.view.updatePaddingRelative
import com.kgbier.kgbmd.Navigator
import com.kgbier.kgbmd.Route
import com.kgbier.kgbmd.util.dp
import com.kgbier.kgbmd.view.ui.CastMemberView

class CastMemberViewModel(
    val name: String,
    val role: String?,
    val avatarUrl: String?,
    val nameId: String?,
) : BaseMediaEntityListItemViewModel {
    fun showCastmemberDetails() {
        nameId?.let { Navigator.navigate(Route.DetailScreen(it)) }
    }
}

class CastMemberViewHolder(context: Context) :
    BaseMediaEntityListItemViewHolder(CastMemberView(context).apply {
        layoutParams = ViewGroup.MarginLayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        )
        updatePaddingRelative(start = 16.dp, top = 6.dp, end = 16.dp, bottom = 6.dp)
    }) {
    val view get() = itemView as CastMemberView

    override fun bind(viewModel: BaseMediaEntityListItemViewModel) {
        if (viewModel !is CastMemberViewModel) return

        view.setText(viewModel.name, viewModel.role)
        view.setAvatarUrl(viewModel.avatarUrl)

        if (viewModel.nameId == null) {
            view.setOnClickListener(null)
            view.isClickable = false
            view.showAction(false)
        } else {
            view.setOnClickListener { viewModel.showCastmemberDetails() }
            view.showAction(true)
        }
    }
}
