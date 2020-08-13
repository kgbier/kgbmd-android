package com.kgbier.kgbmd.view.ui.titledetails

import android.content.Context
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import androidx.core.view.updateMarginsRelative
import com.kgbier.kgbmd.util.dp
import com.kgbier.kgbmd.view.ui.CastMemberView

class CastMemberViewModel(val name: String, val role: String?, val avatarUrl: String?) :
    BaseTitlesViewModel

class CastMemberViewHolder(context: Context) :
    BaseTitlesViewHolder(CastMemberView(context).apply {
        layoutParams = ViewGroup.MarginLayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        ).apply {
            updateMarginsRelative(start = 16.dp, end = 16.dp)
        }
    }) {
    val view get() = itemView as CastMemberView

    override fun bind(viewModel: BaseTitlesViewModel) {
        if (viewModel !is CastMemberViewModel) return

        view.setText(viewModel.name, viewModel.role)
        view.setAvatarUrl(viewModel.avatarUrl)
    }
}
