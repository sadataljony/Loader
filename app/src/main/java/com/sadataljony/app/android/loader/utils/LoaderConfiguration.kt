package com.sadataljony.app.android.loader.utils

import android.content.Context
import android.graphics.Color
import com.sadataljony.app.android.loader.utils.SimpleArcLoader.STYLE
import com.sadataljony.app.android.myapplication.R

class LoaderConfiguration(context: Context) {
    var loaderStyle: STYLE = STYLE.SIMPLE_ARC
    var arcMargin = 0
    var animationSpeed = 0
    var arcWidth = 0
    private var arcCircle = false
    private var _colors = intArrayOf(
        Color.parseColor("#F90101"),
        Color.parseColor("#0266C8"),
        Color.parseColor("#F2B50F"),
        Color.parseColor("#00933B")
    )

    fun setAnimationSpeedWithIndex(mAnimationIndex: Int) {
        when (mAnimationIndex) {
            0 -> animationSpeed = SimpleArcLoader.SPEED_SLOW
            1 -> animationSpeed = SimpleArcLoader.SPEED_MEDIUM
            2 -> animationSpeed = SimpleArcLoader.SPEED_FAST
        }
    }

    fun setArcWidthInPixel(mStrokeWidth: Int) {
        arcWidth = mStrokeWidth
    }

    var colors: IntArray
        get() = _colors
        set(colors) {
            if (colors.isNotEmpty()) _colors = colors
        }

    fun drawCircle(): Boolean {
        return arcCircle
    }

    fun drawCircle(drawCircle: Boolean) {
        arcCircle = drawCircle
    }

    init {
        arcMargin = SimpleArcLoader.MARGIN_MEDIUM
        animationSpeed = SimpleArcLoader.SPEED_MEDIUM
        arcWidth = context.resources.getDimension(R.dimen.stroke_width).toInt()
    }
}