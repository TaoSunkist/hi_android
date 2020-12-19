package me.taosunkist.hello.utility

import android.content.Context
import android.graphics.*
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.TypedValue
import androidx.core.content.res.ResourcesCompat
import com.watermark.androidwm_light.bean.WatermarkText
import java.io.File
import kotlin.math.roundToInt

object BitmapUtility {

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {

            val heightRatio = (height.toFloat() / reqHeight.toFloat()).roundToInt()
            val widthRatio = (width.toFloat() / reqWidth.toFloat()).roundToInt()

            inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
        }

        return inSampleSize
    }

    fun compressedBitmap(file: File, reqWidth: Int, reqHeight: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(file.path, options)
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeFile(file.path, options)
    }

    /**
     * 把文字转换成bitmap
     *
     * @return [Bitmap] the bitmap return.
     */
    fun textAsBitmap(context: Context, watermarkText: WatermarkText): Bitmap {
        val watermarkPaint = TextPaint()
        watermarkPaint.color = watermarkText.textColor
        watermarkPaint.style = watermarkText.textStyle

        if (watermarkText.textAlpha >= 0 && watermarkText.textAlpha <= 255) {
            watermarkPaint.alpha = watermarkText.textAlpha
        }

        val value = watermarkText.textSize.toFloat()
        val pixel = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                value, context.resources.displayMetrics).toInt()
        watermarkPaint.textSize = pixel.toFloat()

        if (watermarkText.textShadowBlurRadius != 0f
                || watermarkText.textShadowXOffset != 0f
                || watermarkText.textShadowYOffset != 0f) {
            watermarkPaint.setShadowLayer(watermarkText.textShadowBlurRadius,
                    watermarkText.textShadowXOffset,
                    watermarkText.textShadowYOffset,
                    watermarkText.textShadowColor)
        }

        if (watermarkText.textFont != 0) {
            val typeface = ResourcesCompat.getFont(context, watermarkText.textFont)
            watermarkPaint.typeface = typeface
        }

        watermarkPaint.isAntiAlias = true
        watermarkPaint.textAlign = Paint.Align.LEFT
        watermarkPaint.strokeWidth = 5f

        val baseline = (-watermarkPaint.ascent() + 1f).toInt().toFloat()
        val bounds = Rect()
        watermarkPaint.getTextBounds(watermarkText.text,
                0, watermarkText.text.length, bounds)

        var boundWidth = bounds.width() + 20
        val mTextMaxWidth = watermarkPaint.measureText(watermarkText.text).toInt()
        if (boundWidth > mTextMaxWidth) {
            boundWidth = mTextMaxWidth
        }
        val staticLayout = StaticLayout(watermarkText.text,
                0, watermarkText.text.length,
                watermarkPaint, mTextMaxWidth, Layout.Alignment.ALIGN_NORMAL, 2.0f,
                2.0f, false)

        val lineCount = staticLayout.lineCount
        val height = (baseline + watermarkPaint.descent() + 3f).toInt() * lineCount
        var image = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        if (boundWidth > 0 && height > 0) {
            image = Bitmap.createBitmap(boundWidth, height, Bitmap.Config.ARGB_8888)
        }
        val canvas = Canvas(image)
        canvas.drawColor(watermarkText.backgroundColor)
        staticLayout.draw(canvas)
        return image
    }
}