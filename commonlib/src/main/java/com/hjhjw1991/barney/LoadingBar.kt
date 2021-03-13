package com.hjhjw1991.barney

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.annotation.Keep

class BarneyLoadingView: View {
    private lateinit var rectFBg: RectF
    private lateinit var rectFProgress: RectF
    private lateinit var mPaint: Paint
    private var mWidth = 0
    private var progressPercent = 0f
    private var bgColor = 0
    private var progressColor = 0
    private var mHeight = 0
    private var radius = 0
    private var startColor = 0
    private var endColor = 0
    private var gradient: LinearGradient? = null
    private var isGradient = false

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        //获取自定义属性
        val typedArray: TypedArray =
            context.obtainStyledAttributes(attrs, R.styleable.BarneyLoadingView)
        bgColor = typedArray.getColor(
            R.styleable.BarneyLoadingView_barBgColor,
            resources.getColor(R.color.gray_cfcfcf)
        )
        progressColor = typedArray.getColor(
            R.styleable.BarneyLoadingView_barProgressColor,
            resources.getColor(R.color.orange_ffc032)
        )
        mHeight = typedArray.getDimensionPixelSize(
            R.styleable.BarneyLoadingView_barHeight,
            context.resources.getDimensionPixelSize(R.dimen._10dp)
        )
        isGradient = typedArray.getBoolean(R.styleable.BarneyLoadingView_barIsGradient, false)
        startColor = typedArray.getColor(
            R.styleable.BarneyLoadingView_barStartColor,
            resources.getColor(R.color.black_3A3D4E)
        )
        endColor = typedArray.getColor(
            R.styleable.BarneyLoadingView_barEndColor,
            resources.getColor(R.color.black_475B80)
        )
        radius = typedArray.getInt(R.styleable.BarneyLoadingView_barRadius, RADIUS)
        typedArray.recycle()
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSpecMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSpecSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSpecMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSpecSize = MeasureSpec.getSize(heightMeasureSpec)
        mWidth = if (widthSpecMode == MeasureSpec.EXACTLY || widthSpecMode == MeasureSpec.AT_MOST) {
            widthSpecSize
        } else {
            0
        }
        mHeight =
            if (heightSpecMode == MeasureSpec.AT_MOST || heightSpecMode == MeasureSpec.UNSPECIFIED) {
                heightSpecSize
            } else {
                getContext().getResources().getDimensionPixelSize(R.dimen._10dp)
            }
        setMeasuredDimension(mWidth, mHeight)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        gradient =
            LinearGradient(0f, 0f, width.toFloat(), mHeight.toFloat(), startColor, endColor, Shader.TileMode.CLAMP)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //1、背景
        mPaint.shader = null
        mPaint.color = bgColor
        rectFBg.right = mWidth.toFloat() //宽度
        rectFBg.bottom = mHeight.toFloat() //高度
        canvas.drawRoundRect(rectFBg, radius.toFloat(), radius.toFloat(), mPaint)
        //2、进度条
        rectFProgress.right = mWidth * progressPercent
        rectFProgress.bottom = mHeight.toFloat()
        //3、是否绘制渐变色
        if (isGradient) {
            mPaint.shader = gradient //设置线性渐变
        } else {
            mPaint.color = progressColor
        }
        if (progressPercent > 0 && rectFProgress.right < radius) //进度值小于半径时，设置大于半径的最小值，防止绘制不出圆弧矩形
            rectFProgress.right = (radius + 10).toFloat()
        canvas.drawRoundRect(rectFProgress, radius.toFloat(), radius.toFloat(), mPaint) //进度}
    }

    @Keep
    fun setPercentage(percentage: Float) {
        progressPercent = if (percentage / MAX >= 1) {
            1f
        } else {
            percentage / MAX
        }
        invalidate()
    }

    private fun init() {
        rectFBg = RectF(0f, 0f, 0f, mHeight.toFloat())
        rectFProgress = RectF(0f, 0f, 0f, mHeight.toFloat())
        mPaint = Paint()
        //设置抗锯齿
        mPaint.isAntiAlias = true
    }

    fun setHeight(mHeight: Int) {
        this.mHeight = mHeight
        invalidate()
    }

    fun setBgColor(bgColor: Int) {
        this.bgColor = bgColor
    }

    fun setProgressColor(progressColor: Int) {
        this.progressColor = progressColor
    }

    fun setStartColor(startColor: Int) {
        this.startColor = startColor
    }

    fun setEndColor(endColor: Int) {
        this.endColor = endColor
    }

    fun setGradient(gradient: Boolean) {
        isGradient = gradient
    }

    companion object {
        const val MAX = 100f
        const val RADIUS = 15 // 圆角矩形半径
    }
}