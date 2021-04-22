package me.taosunkist.hello.ui.reusable

import android.R
import android.content.Context
import android.graphics.Canvas
import android.graphics.CornerPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import me.taosunkist.hello.utility.Dimens.dpToPx
import java.text.DecimalFormat

class SquareProgressView : View {
    private var progress = 0f
    private var progressBarPaint: Paint = Paint()
    private var outlinePaint: Paint = Paint()
    private var textPaint: Paint = Paint()
    private var widthInDp = 10f
    private var strokewidth = 0f
    val path = Path()

    var isOutline = false
        set(outline) {
            field = outline
            this.invalidate()
        }
    var isStartline = false
        set(startline) {
            field = startline
            this.invalidate()
        }
    var isShowProgress = false
        set(showProgress) {
            field = showProgress
            this.invalidate()
        }
    var isCenterline = false
        set(centerline) {
            field = centerline
            this.invalidate()
        }
    var isRoundedCorners = false
        private set
    private var roundedCornersRadius = 10f
    private var percentSettings = PercentStyle(Paint.Align.CENTER, 150f, true)
    var isClearOnHundred = false
        set(clearOnHundred) {
            field = clearOnHundred
            this.invalidate()
        }
    var isIndeterminate = false
        set(indeterminate) {
            field = indeterminate
            this.invalidate()
        }
    private var indeterminateCount = 1
    private val indeterminateWidth = 20.0f

    constructor(context: Context) : super(context) {
        initializePaints(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        initializePaints(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initializePaints(context)
    }

    private fun initializePaints(context: Context) {
        progressBarPaint.color = context.resources.getColor(R.color.holo_green_dark)
        progressBarPaint.strokeWidth = dpToPx(widthInDp.toInt()).toFloat()
        progressBarPaint.isAntiAlias = true
        progressBarPaint.style = Paint.Style.STROKE
        outlinePaint = Paint()
        outlinePaint.color = context.resources.getColor(R.color.black)
        outlinePaint.strokeWidth = 1f
        outlinePaint.isAntiAlias = true
        outlinePaint.style = Paint.Style.STROKE
        textPaint = Paint()
        textPaint.color = context.resources.getColor(R.color.black)
        textPaint.isAntiAlias = true
        textPaint.style = Paint.Style.STROKE
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        strokewidth = dpToPx(widthInDp.toInt()).toFloat()
        val cW = width
        val cH = height
        val scope = 2 * cW + 2 * cH - 4 * strokewidth
        val hSw = strokewidth / 2
        if (isOutline) {
            drawOutline(canvas)
        }
        if (isStartline) {
            drawStartline(canvas)
        }
        if (isShowProgress) {
            drawPercent(canvas, percentSettings)
        }
        if (isCenterline) {
            drawCenterLine(canvas, strokewidth)
        }
        if (isClearOnHundred && progress.toDouble() == 100.0 || progress <= 0.0) {
            return
        }
        if (isIndeterminate) {
            path.reset()
            val drawEnd = getDrawEnd(scope / 100 * java.lang.Float.valueOf(indeterminateCount.toString()), canvas)
            if (drawEnd.place == Place.TOP) {
                path.moveTo(drawEnd.location - indeterminateWidth - strokewidth, hSw)
                path.lineTo(drawEnd.location, hSw)
                canvas.drawPath(path, progressBarPaint)
            }
            if (drawEnd.place == Place.RIGHT) {
                path.moveTo(cW - hSw, drawEnd.location - indeterminateWidth)
                path.lineTo(cW - hSw, strokewidth + drawEnd.location)
                canvas.drawPath(path, progressBarPaint)
            }
            if (drawEnd.place == Place.BOTTOM) {
                path.moveTo(drawEnd.location - indeterminateWidth - strokewidth, cH - hSw)
                path.lineTo(drawEnd.location, cH - hSw)
                canvas.drawPath(path, progressBarPaint)
            }
            if (drawEnd.place == Place.LEFT) {
                path.moveTo(hSw, drawEnd.location - indeterminateWidth - strokewidth)
                path.lineTo(hSw, drawEnd.location)
                canvas.drawPath(path, progressBarPaint)
            }
            indeterminateCount++
            if (indeterminateCount > 100) {
                indeterminateCount = 0
            }
            invalidate()
        } else {
            path.reset()
            val drawEnd = getDrawEnd(scope / 100 * java.lang.Float.valueOf(progress.toString()), canvas)
            if (drawEnd.place == Place.TOP) {
                if (drawEnd.location > cW / 2 && progress < 100.0) {
                    path.moveTo((cW / 2).toFloat(), hSw)
                    path.lineTo(drawEnd.location, hSw)
                } else {
                    path.moveTo((cW / 2).toFloat(), hSw)
                    path.lineTo(cW - hSw, hSw)
                    path.lineTo(cW - hSw, cH - hSw)
                    path.lineTo(hSw, cH - hSw)
                    path.lineTo(hSw, hSw)
                    path.lineTo(strokewidth, hSw)
                    path.lineTo(drawEnd.location, hSw)
                }
                canvas.drawPath(path, progressBarPaint)
            }
            if (drawEnd.place == Place.RIGHT) {
                path.moveTo((cW / 2).toFloat(), hSw)
                path.lineTo(cW - hSw, hSw)
                path.lineTo(cW - hSw, 0 + drawEnd.location)
                canvas.drawPath(path, progressBarPaint)
            }
            if (drawEnd.place == Place.BOTTOM) {
                path.moveTo((cW / 2).toFloat(), hSw)
                path.lineTo(cW - hSw, hSw)
                path.lineTo(cW - hSw, cH - hSw)
                path.lineTo(cW - strokewidth, cH - hSw)
                path.lineTo(drawEnd.location, cH - hSw)
                canvas.drawPath(path, progressBarPaint)
            }
            if (drawEnd.place == Place.LEFT) {
                path.moveTo((cW / 2).toFloat(), hSw)
                path.lineTo(cW - hSw, hSw)
                path.lineTo(cW - hSw, cH - hSw)
                path.lineTo(hSw, cH - hSw)
                path.lineTo(hSw, cH - strokewidth)
                path.lineTo(hSw, drawEnd.location)
                canvas.drawPath(path, progressBarPaint)
            }
        }
    }

    private fun drawStartline(canvas: Canvas) {
        val outlinePath = Path()
        outlinePath.moveTo((width / 2).toFloat(), 0f)
        outlinePath.lineTo((width / 2).toFloat(), strokewidth)
        canvas.drawPath(outlinePath, outlinePaint)
    }

    private fun drawOutline(canvas: Canvas) {
        val outlinePath = Path()
        outlinePath.moveTo(0f, 0f)
        outlinePath.lineTo(width.toFloat(), 0f)
        outlinePath.lineTo(width.toFloat(), height.toFloat())
        outlinePath.lineTo(0f, height.toFloat())
        outlinePath.lineTo(0f, 0f)
        outlinePaint.style = Paint.Style.FILL
        canvas.drawPath(outlinePath, outlinePaint)
    }

    fun getProgress(): Float {
        return progress
    }

    fun setProgress(progress: Float) {
        this.progress = progress
        this.invalidate()
    }

    fun setColor(color: Int) {
        progressBarPaint.color = color
        this.invalidate()
    }

    fun setWidthInDp(width: Int) {
        widthInDp = width.toFloat()
        progressBarPaint.strokeWidth = dpToPx(widthInDp.toInt()).toFloat()
        this.invalidate()
    }

    private fun drawPercent(canvas: Canvas, setting: PercentStyle) {
        textPaint.textAlign = setting.align
        if (setting.textSize == 0f) {
            textPaint.textSize = (height / 10 * 4).toFloat()
        } else {
            textPaint.textSize = setting.textSize
        }
        var percentString = DecimalFormat("###").format(getProgress().toDouble())
        if (setting.isPercentSign) {
            percentString += percentSettings.customText
        }
        textPaint.color = percentSettings.textColor
        canvas.drawText(percentString,
            (width / 2).toFloat(),
            (height / 2 - (textPaint.descent() + textPaint.ascent()) / 2),
            textPaint)
    }

    var percentStyle: PercentStyle
        get() = percentSettings
        set(percentSettings) {
            this.percentSettings = percentSettings
            this.invalidate()
        }

    private fun drawCenterLine(canvas: Canvas, strokeWidth: Float) {
        val centerOfStrokeWidth = strokeWidth / 2
        val centerLinePath = Path()
        centerLinePath.moveTo(centerOfStrokeWidth, centerOfStrokeWidth)
        centerLinePath.lineTo(width - centerOfStrokeWidth, centerOfStrokeWidth)
        centerLinePath.lineTo(width - centerOfStrokeWidth, height - centerOfStrokeWidth)
        centerLinePath.lineTo(centerOfStrokeWidth, height - centerOfStrokeWidth)
        centerLinePath.lineTo(centerOfStrokeWidth, centerOfStrokeWidth)
        canvas.drawPath(centerLinePath, outlinePaint)
    }

    private fun getDrawEnd(percent: Float, canvas: Canvas?): DrawStop {
        val drawStop = DrawStop()
        strokewidth = dpToPx(widthInDp.toInt()).toFloat()
        val halfOfTheImage = (width / 2).toFloat()

        // top right
        if (percent > halfOfTheImage) {
            val second = percent - halfOfTheImage

            // right
            if (second > height - strokewidth) {
                val third = second - (height - strokewidth)

                // bottom
                if (third > width - strokewidth) {
                    val forth = third - (width - strokewidth)

                    // left
                    if (forth > height - strokewidth) {
                        val fifth = forth - (height - strokewidth)

                        // top left
                        if (fifth == halfOfTheImage) {
                            drawStop.place = Place.TOP
                            drawStop.location = halfOfTheImage
                        } else {
                            drawStop.place = Place.TOP
                            drawStop.location = strokewidth + fifth
                        }
                    } else {
                        drawStop.place = Place.LEFT
                        drawStop.location = height - strokewidth - forth
                    }
                } else {
                    drawStop.place = Place.BOTTOM
                    drawStop.location = width - strokewidth - third
                }
            } else {
                drawStop.place = Place.RIGHT
                drawStop.location = strokewidth + second
            }
        } else {
            drawStop.place = Place.TOP
            drawStop.location = halfOfTheImage + percent
        }
        return drawStop
    }

    fun setRoundedCorners(roundedCorners: Boolean, radius: Float) {
        isRoundedCorners = roundedCorners
        roundedCornersRadius = radius
        if (roundedCorners) {
            progressBarPaint.pathEffect = CornerPathEffect(roundedCornersRadius)
        } else {
            progressBarPaint.pathEffect = null
        }
        this.invalidate()
    }

    inner class DrawStop {
        var place: Place? = null
        var location = 0f
    }

    enum class Place {
        TOP, RIGHT, BOTTOM, LEFT
    }

    fun setProgressBarColor(@ColorInt color: Int) {
        progressBarPaint.color = color
    }
}