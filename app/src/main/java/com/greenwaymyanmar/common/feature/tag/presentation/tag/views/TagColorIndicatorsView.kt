package com.greenwaymyanmar.common.feature.tag.presentation.tag.views

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.Space
import androidx.annotation.ColorInt
import greenway_myanmar.org.R

class TagColorIndicatorsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val horizontalSpace4dp = Space(context).apply {
        val margin = resources.getDimensionPixelSize(R.dimen.spacing_4)
        layoutParams = MarginLayoutParams(margin, 0)
    }

    private val circleSize = resources.getDimensionPixelSize(R.dimen.spacing_8)
    private val elevationSize = resources.getDimensionPixelSize(R.dimen.spacing_4)

    init {
        orientation = HORIZONTAL
    }

    fun setTags(colors: List<Int>) {
        removeAllViews()
        colors.forEachIndexed { index, color ->
            if (index > 0) {
                addView(horizontalSpace4dp)
            }
            addView(createColorCircle(color))
        }
    }

    private fun createColorCircle(@ColorInt color: Int) = TagColorIndicatorView(context).apply {
        setCircleColor(color)
    }

}