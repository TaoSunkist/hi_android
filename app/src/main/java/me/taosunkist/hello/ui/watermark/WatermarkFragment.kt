package me.taosunkist.hello.ui.watermark

import android.content.res.Resources
import android.graphics.drawable.BitmapDrawable
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import com.watermark.androidwm_light.WatermarkBuilder

import me.taosunkist.hello.R
import com.watermark.androidwm_light.bean.WatermarkText
import android.graphics.Color
import com.watermark.androidwm_light.bean.WatermarkImage
import com.watermark.androidwm_light.utils.BitmapUtils
import com.watermark.androidwm_light.bean.WatermarkPosition

class WatermarkFragment : Fragment() {

    companion object {
        val tag: String = WatermarkFragment::class.java.simpleName

        fun newInstance() = WatermarkFragment()
    }

    private lateinit var viewModel: WatermarkViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.watermark_fragment, container, false)
    }

    val drawableResIds = listOf(R.drawable.bg_default, R.drawable.qq20191031, R.drawable.qq20191032, R.drawable.qq20191033)
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
            val bgDefaultBitmapWidth = bgDefaultBitmap.width
            val bgDefaultBitmapHeight = bgDefaultBitmap.height

            val logoMargin = 0.0

            val waterMarkBitmap = (context!!.getDrawable(R.drawable.tatame_watermark) as BitmapDrawable).bitmap
            val newWatermarkBitmap = BitmapUtils.resizeBitmap(waterMarkBitmap, waterMarkBitmap.width.toFloat() / bgDefaultBitmap.width.toFloat(), bgDefaultBitmap)
            val watermarkImage = WatermarkImage(newWatermarkBitmap).setImageAlpha(255).setPosition(WatermarkPosition(0.5, logoMargin))
//            println("taohui image watermark: x${logoMargin},y:${logoMargin}")

            val watermarkText = WatermarkText("ID:1046PO").setTextColor(Color.BLACK).setTextAlpha(255)
            val watermarkTextBitmap = BitmapUtils.textAsBitmap(context, watermarkText)

            val newWatermarkTextBitmap = BitmapUtils.resizeBitmap(watermarkTextBitmap, waterMarkBitmap.width.toFloat() / bgDefaultBitmapWidth.toFloat(), bgDefaultBitmap)

            val textMargin = (newWatermarkTextBitmap.height).toDouble() / (bgDefaultBitmapHeight - newWatermarkBitmap.height)
            val newWatermarkText = WatermarkImage(newWatermarkTextBitmap).setPosition(WatermarkPosition(0.5, textMargin)).setImageAlpha(255)
            println("taohui text watermark: x${logoMargin},y:${textMargin}")

            println("taohui ${bgDefaultBitmapHeight}, ${newWatermarkBitmap.height}, ${newWatermarkTextBitmap.height}")
            val watermark = WatermarkBuilder.create(context, bgDefaultBitmap).loadWatermarkImages(listOf(newWatermarkText, watermarkImage)).watermark
            targetImageView.setImageBitmap(watermark.outputImage)
            drawableResIdsIndex++

        }

    }

    fun pxToDp(px: Int): Float {
        return (px / Resources.getSystem().displayMetrics.density)
    }

    fun dpToPx(dp: Int): Float {
        return (dp * Resources.getSystem().displayMetrics.density)
    }
}
