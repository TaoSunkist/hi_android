package me.taosunkist.hello.ui.frgment.watermark

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import me.taosunkist.hello.R


class WatermarkFragment : Fragment() {

    companion object {
        val tag: String = WatermarkFragment::class.java.simpleName

        fun newInstance() = WatermarkFragment()

        fun pxToDp(px: Int): Float {
            return (px / Resources.getSystem().displayMetrics.density)
        }

        fun dpToPx(dp: Int): Float {
            return (dp * Resources.getSystem().displayMetrics.density)
        }
    }

    private lateinit var viewModel: WatermarkViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_watermark, container, false)
    }

    val drawableResIds = listOf(R.drawable.bg_frontline_default, R.drawable.qq20191031, R.drawable.qq20191032, R.drawable.qq20191033)
    var drawableResIdsIndex: Int = 0
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

//        drawableResIds.forEach {
//            BitmapFactory.decodeResource(resources,it,)
//        }

        viewModel = ViewModelProviders.of(this).get(WatermarkViewModel::class.java)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val targetImageView = view.findViewById<AppCompatImageView>(R.id.target)

        view.findViewById<AppCompatButton>(R.id.make).setOnClickListener {
            if (drawableResIdsIndex >= drawableResIds.size) drawableResIdsIndex = 0

            val bgDefaultBitmap = (context!!.getDrawable(drawableResIds[drawableResIdsIndex]) as BitmapDrawable).bitmap
//            val bgDefaultBitmapWidth = bgDefaultBitmap.width
//            val bgDefaultBitmapHeight = bgDefaultBitmap.height
//            val logoMargin = 0.0
//            val waterMarkBitmap = (context!!.getDrawable(R.drawable.tatame_watermark) as BitmapDrawable).bitmap
//            val newWatermarkBitmap = BitmapUtils.resizeBitmap(waterMarkBitmap, waterMarkBitmap.width.toFloat() / bgDefaultBitmap.width.toFloat(), bgDefaultBitmap)
//            val watermarkImage = WatermarkImage(newWatermarkBitmap).setImageAlpha(255).setPosition(WatermarkPosition(0.5, logoMargin))
//
//            val watermarkText = WatermarkText("ID:1046PO").setTextColor(Color.BLACK).setTextAlpha(255)
            val watermarkTextBitmap = textAsBitmap(activity!!, "ID:1046P0")
//
//            val newWatermarkTextBitmap = BitmapUtils.resizeBitmap(watermarkTextBitmap, waterMarkBitmap.width.toFloat() / bgDefaultBitmapWidth.toFloat(), bgDefaultBitmap)
//
//            val textMargin = (newWatermarkTextBitmap.height).toDouble() / (bgDefaultBitmapHeight - newWatermarkBitmap.height)
//            val newWatermarkText = WatermarkImage(newWatermarkTextBitmap).setPosition(WatermarkPosition(0.5, textMargin)).setImageAlpha(255)
//            println("taohui text watermark: x${logoMargin},y:${textMargin}, ${context!!.cacheDir.path}")
//
//            println("taohui ${bgDefaultBitmapHeight}, ${newWatermarkBitmap.height}, ${newWatermarkTextBitmap.height}")
//            val watermark = WatermarkBuilder.create(context, bgDefaultBitmap).loadWatermarkImages(listOf(newWatermarkText, watermarkImage)).watermark
//            watermark.saveToLocalPng(context!!.cacheDir.path)
//            targetImageView.setImageBitmap(watermark.outputImage)

            targetImageView.setImageBitmap(addWaterMark(activity!!, bgDefaultBitmap, watermarkTextBitmap))
            drawableResIdsIndex++
        }
    }

    fun addWaterMark(activity: FragmentActivity, srcBitmap: Bitmap, textWatermarkBitmap: Bitmap): Bitmap {
        val w = srcBitmap.width
        val h = srcBitmap.height
        val result = Bitmap.createBitmap(w, h, srcBitmap.config)
        val canvas = Canvas(result).also { it.drawBitmap(srcBitmap, 0f, 0f, null) }

        val watermarkPadding = w / 30f
        val waterMark = BitmapFactory.decodeResource(activity.resources, R.drawable.tatame_watermark)
        val newWatermarkWidth = w / 6
        val newWatermarkHeight = waterMark.height * newWatermarkWidth / waterMark.width
        val newWatermark = Bitmap.createScaledBitmap(waterMark, newWatermarkWidth, newWatermarkHeight, true)

        canvas.drawBitmap(newWatermark,
                watermarkPadding,
                watermarkPadding, null)

        val newtextWatermarkBitmap = Bitmap.createScaledBitmap(textWatermarkBitmap, newWatermarkWidth, newWatermarkHeight, true)

        canvas.drawBitmap(
                newtextWatermarkBitmap,
                watermarkPadding,
                watermarkPadding + newWatermarkHeight + newWatermarkHeight / 8, null)

        return result
    }


    /**
     * build a bitmap from a text.
     *
     * @return [Bitmap] the bitmap return.
     */
    fun textAsBitmap(context: Context, watermarkText: String): Bitmap {
        val watermarkPaint = TextPaint()
        watermarkPaint.color = Color.BLACK
        watermarkPaint.style = Paint.Style.FILL
        watermarkPaint.alpha = 255

        val value = 20f
        val pixel = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.resources.displayMetrics).toInt()
        watermarkPaint.textSize = pixel.toFloat()

        watermarkPaint.isAntiAlias = true
        watermarkPaint.textAlign = Paint.Align.LEFT
        watermarkPaint.strokeWidth = 5f

        val baseline = (-watermarkPaint.ascent() + 1f).toInt().toFloat()
        val bounds = Rect()
        watermarkPaint.getTextBounds(watermarkText, 0, watermarkText.length, bounds)

        var boundWidth = bounds.width() + 20
        val mTextMaxWidth = watermarkPaint.measureText(watermarkText).toInt()
        if (boundWidth > mTextMaxWidth) {
            boundWidth = mTextMaxWidth
        }
        val staticLayout = StaticLayout(watermarkText,
                0, watermarkText.length,
                watermarkPaint, mTextMaxWidth, Layout.Alignment.ALIGN_NORMAL, 2.0f,
                2.0f, false)

        val lineCount = staticLayout.lineCount
        val height = (baseline + watermarkPaint.descent() + 3f).toInt() * lineCount
        var image = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        if (boundWidth > 0 && height > 0) {
            image = Bitmap.createBitmap(boundWidth, height, Bitmap.Config.ARGB_8888)
        }
        val canvas = Canvas(image)
        canvas.drawColor(Color.TRANSPARENT)
        staticLayout.draw(canvas)
        return image
    }
}
