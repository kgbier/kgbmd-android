package com.kgbier.kgbmd.view.decoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.ViewCompat.getLayoutDirection
import androidx.recyclerview.widget.RecyclerView
import com.kgbier.kgbmd.util.resolveAttribute
import kotlin.math.roundToInt

class MiddleDividerItemDecoration(context: Context, val startInset: Int = 0) :
    RecyclerView.ItemDecoration() {

    companion object {
        private const val DIVIDER_DRAWABLE_ATTR_ID = android.R.attr.listDivider
    }

    private val bounds = Rect()
    private val divider: Drawable? = context.resolveAttribute(DIVIDER_DRAWABLE_ATTR_ID)?.let {
        context.getDrawable(it)
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (divider != null) {
            drawVertical(canvas, parent, divider)
        }
    }

    private fun drawVertical(canvas: Canvas, parent: RecyclerView, divider: Drawable) {
        canvas.save()
        val left: Int
        val right: Int

        var leftInset = 0
        var rightInset = 0
        when (getLayoutDirection(parent)) {
            ViewCompat.LAYOUT_DIRECTION_LTR -> {
                leftInset = startInset
            }
            ViewCompat.LAYOUT_DIRECTION_RTL -> {
                rightInset = startInset
            }
        }

        if (parent.clipToPadding) {
            left = parent.paddingLeft
            right = parent.width - parent.paddingRight
            canvas.clipRect(
                left, parent.paddingTop, right,
                parent.height - parent.paddingBottom
            )
        } else {
            left = 0
            right = parent.width
        }

        val childCount = parent.childCount
        for (i in 0 until childCount) {
            if (i == childCount - 1) continue
            val child = parent.getChildAt(i)
            parent.getDecoratedBoundsWithMargins(child, bounds)
            val bottom = bounds.bottom + child.translationY.roundToInt()
            val top = bottom - divider.intrinsicHeight
            divider.setBounds(left + leftInset, top, right + rightInset, bottom)
            divider.draw(canvas)
        }
        canvas.restore()
    }

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (divider == null) {
            outRect.set(0, 0, 0, 0)
            return
        }
        outRect.set(0, 0, 0, divider.intrinsicHeight)
    }
}