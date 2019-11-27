package me.taosunkist.hello.widget

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import me.taosunkist.hello.Dimens.dpToPx
import me.taosunkist.hello.R
import android.widget.FrameLayout

data class EnergyViewUIModel(val energyCount: Int) {
    constructor() : this(energyCount = (0..2).random())
}

class EnergyView : FrameLayout {

    private val maxEnergyCount = 3
    private val spaceWidth = dpToPx(16)
    private val energyBars = arrayListOf<ProgressBar>().apply {
        for (i in 0 until maxEnergyCount) {
            this.add(ProgressBar(context, null, android.R.style.Widget_ProgressBar_Horizontal).apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) min = 0 else progress = 0
                max = 1
                progressDrawable = context.getDrawable(R.drawable.ic_energy_bar)
            })
        }
    }
    private val barTitle = TextView(context).apply {
        this.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        text = "电量"
        setTextColor(Color.WHITE)
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        addView(barTitle)
        energyBars.forEach {
            addView(it)
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        val height = b - t
        val barTitleWidth = barTitle.measuredWidth
        energyBars.forEachIndexed { index, progressBar ->
            progressBar.setFrame((barTitleWidth + spaceWidth) * index.inc(), height / 3, barTitleWidth, spaceWidth / 2)
            progressBar.progress = 0
        }
    }

    fun bind(uiModel: EnergyViewUIModel) {
        energyBars.forEachIndexed { index, progressBar ->
            if (index < uiModel.energyCount) {
                progressBar.progress = 1
            } else {
                progressBar.progress = 0
            }
        }
    }
}

fun View.setFrame(x: Int, y: Int, width: Int, height: Int) {
    layout(x, y, x + width, y + height)
}