package com.greenwaymyanmar.common.feature.tag.presentation.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import greenway_myanmar.org.R

class TagColorIndicatorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var radius = resources.getDimensionPixelSize(R.dimen.spacing_4).toFloat()
    private var shadowRadius = resources.getDimensionPixelSize(R.dimen.spacing_2).toFloat()
    private var shadowDx = 0f
    private var shadowDy = resources.getDimensionPixelSize(R.dimen.spacing_1).toFloat()
    private var circleColor = Color.RED
    private var shadowColor = Color.parseColor("#33000000")

    private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.TagColorIndicatorView)
        radius = typedArray.getDimension(R.styleable.TagColorIndicatorView_radius, radius)
        shadowRadius = typedArray.getDimension(R.styleable.TagColorIndicatorView_shadowRadius, shadowRadius)
        shadowDx = typedArray.getDimension(R.styleable.TagColorIndicatorView_shadowDx, shadowDx)
        shadowDy = typedArray.getDimension(R.styleable.TagColorIndicatorView_shadowDy, shadowDy)
        circleColor = typedArray.getColor(R.styleable.TagColorIndicatorView_circleColor, circleColor)
        shadowColor = typedArray.getColor(R.styleable.TagColorIndicatorView_shadowColor, shadowColor)

        typedArray.recycle()

        circlePaint.color = circleColor

        setLayerType(LAYER_TYPE_SOFTWARE, null)
        circlePaint.color = circleColor
        shadowPaint.color = shadowColor
        shadowPaint.style = Paint.Style.FILL
        shadowPaint.setShadowLayer(shadowRadius, shadowDx, shadowDy, shadowColor)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val x = (width - radius * 2) / 2f
        val y = (height - radius * 2) / 2f
        canvas.drawCircle(x + radius, y + radius, radius, circlePaint)
        canvas.drawCircle(x + radius, y + radius, radius, shadowPaint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val desiredWidth = (radius * 2 + shadowRadius * 2).toInt()
        val desiredHeight = (radius * 2 + shadowRadius * 2).toInt()

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val width = when (widthMode) {
            MeasureSpec.EXACTLY -> widthSize
            MeasureSpec.AT_MOST -> minOf(widthSize, desiredWidth)
            else -> desiredWidth
        }

        val height = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize
            MeasureSpec.AT_MOST -> minOf(heightSize, desiredHeight)
            else -> desiredHeight
        }

        setMeasuredDimension(width, height)
    }

    fun setRadius(radius: Float) {
        this.radius = radius
        invalidate()
    }

    fun setShadowRadius(shadowRadius: Float) {
        this.shadowRadius = shadowRadius
        shadowPaint.setShadowLayer(shadowRadius, shadowDx, shadowDy, shadowColor)
        invalidate()
    }

    fun setShadowOffset(shadowDx: Float, shadowDy: Float) {
        this.shadowDx = shadowDx
        this.shadowDy = shadowDy
        shadowPaint.setShadowLayer(shadowRadius, shadowDx, shadowDy, shadowColor)
        invalidate()
    }

    fun setCircleColor(color: Int) {
        circleColor = color
        circlePaint.color = color
        invalidate()
    }

    fun setShadowColor(color: Int) {
        shadowColor = color
        shadowPaint.setShadowLayer(shadowRadius, shadowDx, shadowDy, color)
        invalidate()
    }

    fun setElevation(elevation: Int) {
        setShadowRadius((elevation / 2).toFloat())
        setShadowOffset(0f, elevation * 0.5f)
    }

    fun getRadius(): Float {
        return radius
    }

    fun getShadowRadius(): Float {
        return shadowRadius
    }

    fun getShadowOffsetX(): Float {
        return shadowDx
    }

    fun getShadowOffsetY(): Float {
        return shadowDy
    }

    fun getCircleColor(): Int {
        return circleColor
    }

    fun getShadowColor(): Int {
        return shadowColor
    }
}