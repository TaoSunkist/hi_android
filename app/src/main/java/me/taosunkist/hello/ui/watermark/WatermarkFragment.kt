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
import android.widget.Toast
import com.watermark.androidwm_light.bean.WatermarkImage


class WatermarkFragment : Fragment() {

    companion object {
        val tag: String = WatermarkFragment::class.java.simpleName

        fun newInstance() = WatermarkFragment()
    }

    private lateinit var viewModel: WatermarkViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.watermark_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(WatermarkViewModel::class.java)
        val bgDefaultBitmap = (context!!.getDrawable(R.drawable.bg_default) as BitmapDrawable).bitmap
        val waterMarkBitmap = (context!!.getDrawable(R.drawable.tatame_watermark) as BitmapDrawable).bitmap

        val watermarkText = WatermarkText("ID:1046PO")
                .setPositionY(0.08)
                .setPositionX(0.04)
                .setTextColor(Color.WHITE)
                .setTextSize(18.0)
                .setTextAlpha(255)

        val watermarkImage = WatermarkImage(waterMarkBitmap)
                .setPositionY(0.02)
                .setPositionX(0.04)
                .setImageAlpha(255)

        view!!.findViewById<AppCompatButton>(R.id.make).setOnClickListener {
            val targetImageView = view!!.findViewById<AppCompatImageView>(R.id.target)
            val watermark = WatermarkBuilder
                    .create(context, bgDefaultBitmap)
                    .loadWatermarkText(watermarkText)
                    .loadWatermarkImage(watermarkImage)
                    .watermark

            watermark.setToImageView(targetImageView)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val travelButton = view.findViewById<AppCompatImageView>(R.id.fragment_watermark_travel_button)
        travelButton.setOnClickListener {
            travelButton.isEnabled = false
            Toast.makeText(context, "travelButton isEnable:${travelButton.isEnabled}", Toast.LENGTH_SHORT).show()
        }
    }

    fun pxToDp(px: Int): Float {
        return (px / Resources.getSystem().displayMetrics.density)
    }

    fun dpToPx(dp: Int): Float {
        return (dp * Resources.getSystem().displayMetrics.density)
    }
}
