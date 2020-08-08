package com.kgbier.kgbmd.view.drawable

import android.animation.ValueAnimator
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.constraintlayout.solver.widgets.Rectangle
import androidx.core.graphics.translationMatrix
import com.kgbier.kgbmd.util.dp

@ColorInt
private const val LIGHT = 0x22_33_33_33

@ColorInt
private const val DARK = 0x22_00_00_00

class ShimmerDrawable : Drawable() {

    var boundsRectangle: Rect? = null

    private val shimmerPaint by lazy {
        Paint().apply {
            LinearGradient(
                64f.dp, 0f, // line start
                0f, 64f.dp, // line end
                intArrayOf(LIGHT, DARK, LIGHT),
                null, // distribute evenly
                Shader.TileMode.CLAMP
            ).also { shader = it }
        }
    }

    private val translationMatrix = translationMatrix()

    private val shimmerAnimator = ValueAnimator.ofFloat((-360f).dp, 270f.dp).apply {
        repeatMode = ValueAnimator.RESTART
        repeatCount = ValueAnimator.INFINITE
        duration = 1000
    }

    override fun draw(canvas: Canvas) {
        val rect = boundsRectangle ?: return

        val translation = (shimmerAnimator.animatedValue as Float)
        translationMatrix.setTranslate(translation, 0f)
        shimmerPaint.shader.setLocalMatrix(translationMatrix)

        canvas.drawRect(rect, shimmerPaint)
    }

    override fun onBoundsChange(bounds: Rect?) {
        boundsRectangle = bounds
        super.onBoundsChange(bounds)
    }

    private var attached = false

    override fun setVisible(visible: Boolean, restart: Boolean): Boolean {
        if (visible && !attached) {
            attached = true
            shimmerAnimator.start()
            shimmerAnimator.addUpdateListener {
                invalidateSelf()
            }
        } else if(!visible) {
            attached = false
            shimmerAnimator.end()
            shimmerAnimator.removeAllListeners()
        }
        return super.setVisible(visible, restart)
    }

    override fun setAlpha(alpha: Int) {}
    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT
    override fun setColorFilter(colorFilter: ColorFilter?) {}
}
