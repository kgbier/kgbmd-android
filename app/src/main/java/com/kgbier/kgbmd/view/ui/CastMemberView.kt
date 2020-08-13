package com.kgbier.kgbmd.view.ui

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.kgbier.kgbmd.R
import com.kgbier.kgbmd.util.dp
import com.kgbier.kgbmd.util.resolveAttribute
import com.kgbier.kgbmd.util.setTextColorAttr
import com.kgbier.kgbmd.util.setTextStyleAttr

class CastMemberView(context: Context) : LinearLayout(context) {
    val imageViewAvatar: ImageView

    val textViewName: TextView
    val textViewRole: TextView

    init {
        orientation = HORIZONTAL

        imageViewAvatar = ImageView(context).apply {
            layoutParams = LayoutParams(32.dp, 32.dp).apply {
                gravity = Gravity.CENTER_VERTICAL
                marginEnd = 16.dp
            }
        }.also(::addView)

        LinearLayout(context).apply {
            layoutParams =
                LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1f).apply {
                    gravity = Gravity.CENTER_VERTICAL
                }
            orientation = VERTICAL

            textViewName = TextView(context).apply {
                layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
                setTextStyleAttr(R.attr.textAppearanceBody1)
                setTextColorAttr(android.R.attr.textColorPrimary)
            }.also(::addView)

            textViewRole = TextView(context).apply {
                layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
                setTextStyleAttr(R.attr.textAppearanceBody1)
                setTextColorAttr(android.R.attr.textColorSecondary)

                visibility = View.GONE
            }.also(::addView)
        }.also(::addView)
    }

    fun setText(name: String, role: String?) {
        textViewName.text = name
        if (role == null) {
            textViewRole.visibility = View.GONE
        } else {
            textViewRole.visibility = View.VISIBLE
            textViewRole.text = role
        }
    }

    fun setAvatarUrl(avatarUrl: String?) {
        Glide.with(this)
            .load(avatarUrl)
            .placeholder(R.drawable.avatar_placeholder)
            .circleCrop()
            .into(imageViewAvatar)
    }
}
