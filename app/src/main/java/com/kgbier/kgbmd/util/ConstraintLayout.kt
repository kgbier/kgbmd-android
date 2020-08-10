package com.kgbier.kgbmd.util

import android.view.View
import android.view.View.NO_ID
import android.view.View.generateViewId
import androidx.annotation.IdRes
import androidx.annotation.Px
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.ConstraintSet.PARENT_ID

inline class Reference(@IdRes val id: Int)

inline val Reference.start get() = Edge(this, Side(ConstraintSet.START))
inline val Reference.end get() = Edge(this, Side(ConstraintSet.END))
inline val Reference.top get() = Edge(this, Side(ConstraintSet.TOP))
inline val Reference.bottom get() = Edge(this, Side(ConstraintSet.BOTTOM))

inline val ConstrainingScope.start get() = Edge(ref, Side(ConstraintSet.START))
inline val ConstrainingScope.end get() = Edge(ref, Side(ConstraintSet.END))
inline val ConstrainingScope.top get() = Edge(ref, Side(ConstraintSet.TOP))
inline val ConstrainingScope.bottom get() = Edge(ref, Side(ConstraintSet.BOTTOM))

inline class Side(val facing: Int)

class Edge(val ref: Reference, val side: Side)

inline fun ConstraintLayout.constraintSet(block: ConstraintSet.() -> Unit) = ConstraintSet().apply {
    block()
}.applyTo(this)

@Suppress("unused")
inline val ConstraintSet.parent
    get() = Reference(PARENT_ID)

@Suppress("unused")
inline fun ConstraintSet.constrain(ref: Reference, block: ConstrainingScope.() -> Unit) =
    ConstrainingScope(this, ref).block()

@Suppress("unused")
inline fun ConstraintSet.constrain(view: View, block: ConstrainingScope.() -> Unit): Reference {
    val ref = ref(view)
    ConstrainingScope(this, ref).block()
    return ref
}

class ConstrainingScope(val constraintSet: ConstraintSet, val ref: Reference)

@Suppress("unused")
inline fun ConstraintSet.ref(view: View): Reference {
    if (view.id == NO_ID) {
        view.id = generateViewId()
    }
    return Reference(view.id)
}

inline fun ConstrainingScope.link(thisEdge: Edge, otherEdge: Edge, @Px margin: Int? = null) {
    if (margin == null) {
        constraintSet.connect(
            ref.id,
            thisEdge.side.facing,
            otherEdge.ref.id,
            otherEdge.side.facing
        )
    } else {
        constraintSet.connect(
            ref.id,
            thisEdge.side.facing,
            otherEdge.ref.id,
            otherEdge.side.facing,
            margin
        )
    }
}

inline fun ConstrainingScope.visibility(visibility: Int) {
    constraintSet.setVisibility(ref.id, visibility)
}

inline fun ConstrainingScope.ratio(ratio: String) {
    constraintSet.setDimensionRatio(ref.id, ratio)
}

inline fun ConstrainingScope.width(@Px width: Int) {
    constraintSet.constrainWidth(ref.id, width)
}

inline fun ConstrainingScope.height(@Px height: Int) {
    constraintSet.constrainHeight(ref.id, height)
}
