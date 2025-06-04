package com.kgbier.kgbmd.view.ui

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.kgbier.kgbmd.R
import com.kgbier.kgbmd.util.dp
import com.kgbier.kgbmd.util.resolveAttribute
import com.kgbier.kgbmd.util.setTextColorAttr
import com.kgbier.kgbmd.util.setTextStyleAttr

class CastMemberView(context: Context) : LinearLayout(context) {
    val imageViewAvatar: ImageView
    val imageViewOpenInNew: ImageView

    val textViewName: TextView
    val textViewRole: TextView

    init {
        orientation = HORIZONTAL

        background = resolveAttribute(android.R.attr.selectableItemBackground)
            ?.let { ContextCompat.getDrawable(context, it) }

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
                setTextStyleAttr(com.google.android.material.R.attr.textAppearanceBody1)
                setTextColorAttr(android.R.attr.textColorPrimary)
            }.also(::addView)

            textViewRole = TextView(context).apply {
                layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
                setTextStyleAttr(com.google.android.material.R.attr.textAppearanceBody1)
                setTextColorAttr(android.R.attr.textColorSecondary)

                visibility = View.GONE
            }.also(::addView)
        }.also(::addView)

        imageViewOpenInNew = ImageView(context).apply {
            layoutParams = LayoutParams(24.dp, 24.dp).apply {
                gravity = Gravity.CENTER_VERTICAL
                marginStart = 16.dp
            }
            visibility = View.GONE
            setImageResource(R.drawable.ic_chevron_right)
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

    fun showAction(isVisible: Boolean) = if (isVisible) {
        imageViewOpenInNew.visibility = View.VISIBLE
    } else {
        imageViewOpenInNew.visibility = View.GONE
    }
}
