package me.taosunkist.hello.ui.reusable

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import me.taosunkist.hello.utility.Dimens
import me.taosunkist.hello.utility.printf

class GuideTipeLineView : View {

	enum class GuideType {
		ERROR,
		BINGO,
		COUNT_DOWN
	}

	constructor(context: Context?) : super(context)
	constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
	constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
	constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

	val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
		color = Color.BLUE
	}

	val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
		color = Color.BLUE
	}

	var guideType: GuideType = GuideType.ERROR

	val circleRadius = 10f

	init {

		paint.strokeWidth = Dimens.dpToPx(1).toFloat()
		// The gesture threshold expressed in dip
		val GESTURE_THRESHOLD_DIP = 24.0f
		val scale = context.resources.displayMetrics.density;
		textPaint.textSize = (GESTURE_THRESHOLD_DIP * scale + 0.5f);

	}


	override fun onDraw(canvas: Canvas) {
		super.onDraw(canvas)

		var startX = 0f
		var startY = Dimens.dpToPx(25).toFloat()
		var stopX = Dimens.dpToPx(45).toFloat()
		var stopY = Dimens.dpToPx(25).toFloat() + Dimens.dpToPx(30).toFloat()

		if (guideType == GuideType.ERROR) {
			canvas.drawLine(
				startX,
				startY,
				stopX,
				stopY,
				paint)
			startX = stopX
			startY = stopY
			stopX = Dimens.dpToPx(45).toFloat() * 2
			stopY = stopY
			canvas.drawLine(
				startX,
				startY,
				stopX,
				stopY,
				paint)
			canvas.drawCircle(
				stopX,
				stopY,
				circleRadius,
				paint)
			canvas.drawText("错误", stopX + stopX * 0.12f, stopY + stopY * 0.2f, textPaint)
		}


		if (guideType == GuideType.ERROR) {
			startX = width.toFloat()
			startY = Dimens.dpToPx(25).toFloat()
			stopX = width - Dimens.dpToPx(45).toFloat()
			stopY = Dimens.dpToPx(25).toFloat() + Dimens.dpToPx(30).toFloat()
			canvas.drawLine(
				startX,
				startY,
				stopX,
				stopY,
				paint)
			startX = stopX
			startY = stopY
			stopX = width - Dimens.dpToPx(45).toFloat() * 2
			stopY = stopY
			canvas.drawLine(
				startX,
				startY,
				stopX,
				stopY,
				paint)
			canvas.drawCircle(
				stopX,
				stopY,
				circleRadius,
				paint)
			canvas.drawText("正确", width - Dimens.dpToPx(45).toFloat() * 3.4f, stopY + stopY * 0.2f, textPaint)
		}


		if (guideType == GuideType.ERROR) {
			canvas.drawLine(
				width / 2f,
				0f,
				width / 2f,
				Dimens.dpToPx(80).toFloat(),
				paint)

			canvas.drawCircle(
				width / 2f,
				Dimens.dpToPx(80).toFloat(),
				circleRadius,
				paint)
			canvas.drawText("计时", width / 2f - Dimens.dpToPx(25), Dimens.dpToPx(110).toFloat(), textPaint)
		}

	}

	fun showGuideType(guideType: GuideType) {
		this.guideType = guideType
		invalidate()
	}
}