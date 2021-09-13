package me.taosunkist.hello.ui.shakemusicbar

import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.widget.LinearLayout
import me.taosunkist.hello.R
import java.lang.RuntimeException
import java.util.ArrayList
import kotlin.jvm.JvmOverloads

class ShakeMusicBarView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    private val mBars: MutableList<IndividualMusicShakeBar> = ArrayList()
    private val mHandler: Handler = Handler(Looper.getMainLooper())
    private var mOnListener: OnInitFinishedListener? = null
    var invBetweenBars = 0
        private set
    var barCount = 0
        private set
    var color = 0
        private set
    private var mBackgroundDrawable = -1
    private var mVelocity = VELOCITY_DEFAULT
    private var mCalculatedBarWidth = 0

    interface OnInitFinishedListener {
        fun onInitFinished()
    }

    private fun setupBarViewConfiguration(context: Context, attrs: AttributeSet) {
        val barViewAttributes = context.obtainStyledAttributes(attrs, R.styleable.ShakeMusicBarView)
        barCount = barViewAttributes.getInteger(R.styleable.ShakeMusicBarView_barCount, 0)
        invBetweenBars = barViewAttributes.getInteger(R.styleable.ShakeMusicBarView_barInvsInPx, 0)
        color = barViewAttributes.getColor(R.styleable.ShakeMusicBarView_barColor, 0)
        mVelocity = barViewAttributes.getInteger(R.styleable.ShakeMusicBarView_barVelocity, VELOCITY_DEFAULT.toInt()).toFloat()
        barViewAttributes.recycle()
    }

    private fun setupDefaultConfigurationIfNeed() {
        if (barCount == 0) {
            barCount = 1
        }
        if (color == 0) {
            color = Color.GREEN
        }
    }
    /**
     * Initialize using specified color, intervals between bars, the number of bars
     *
     * @param color          the color value
     * @param invBetweenBars the pixel interval between bars
     * @param barCount       number of bars
     */
    /**
     * Initialize using default color, intervals between bars, the number of bars
     */
    @JvmOverloads
    fun init(color: Int = this.color, invBetweenBars: Int = this.invBetweenBars, barCount: Int = this.barCount) {
        this.invBetweenBars = invBetweenBars
        this.barCount = barCount
        this.color = color
        runShakingTask()
    }

    private fun runShakingTask() {
        post {
            initIndividualBars()
            shake(true)
            setupBarDrawable()
            setupInitCallback()
        }
    }

    private fun initIndividualBars() {
        createIndidualBars(barCount)
        val barViewWidth = width.toFloat()
        val numberOfIntervalsBetweenBars = barCount - 1
        val indidvualBarWidth = barViewWidth - numberOfIntervalsBetweenBars * invBetweenBars
        mCalculatedBarWidth = Math.round(indidvualBarWidth / barCount.toFloat())
        for (i in mBars.indices) {
            val currentBar = mBars[i]
            initBarViewPropertyAndAddToParent(i, currentBar)
            currentBar.init(color, mVelocity)
        }
    }

    private fun createIndidualBars(count: Int) {
        mBars.clear()
        for (i in 0 until count) {
            mBars.add(IndividualMusicShakeBar(context, mHandler))
        }
    }

    private fun initBarViewPropertyAndAddToParent(numberOfBar: Int, currentBar: IndividualMusicShakeBar) {
        val mBarLayoutParams = createLinearLayoutParams(mCalculatedBarWidth)
        val actualBarIntervals = calculateActualBarInterval(numberOfBar)
        mBarLayoutParams.setMargins(0, 0, actualBarIntervals, 0)
        addView(currentBar, mBarLayoutParams)
    }

    private fun calculateActualBarInterval(numberOfBar: Int): Int {
        val isLastBar = numberOfBar < mBars.size - 1
        return if (isLastBar) invBetweenBars else 0
    }

    private fun setupInitCallback() {
        if (mOnListener != null) {
            mOnListener!!.onInitFinished()
        }
    }

    private fun setupBarDrawable() {
        if (mBackgroundDrawable >= 0) {
            applyBarDrawable(mBackgroundDrawable)
        }
    }

    fun setOnInitFinishedListener(initListener: OnInitFinishedListener?) {
        mOnListener = initListener
    }

    /**
     * Start or Stop shaking
     *
     * @param isShake This true indicated start to shake, false is stop shaking.
     */
    fun shake(isShake: Boolean) {
        for (bar in mBars) {
            bar.shake(isShake)
        }
    }

    /**
     * Set the specified velocity to all of bars inside this view.
     *
     * @param velocity the moving pixel distance per second
     */
    fun setVelocity(velocity: Int) {
        mVelocity = velocity.toFloat()
        for (bar in mBars) {
            bar.velocity = velocity.toFloat()
        }
    }

    /**
     * After stop shaking, this method make all of bars still to specified pixel height.
     *
     * @param yInPX the height stopped to.
     */
    fun stopToHeight(yInPX: Float) {
        for (bar in mBars) {
            bar.stopToHeight(yInPX)
        }
    }

    private fun createLinearLayoutParams(widthOfBar: Int): LayoutParams {
        return LayoutParams(widthOfBar, LayoutParams.WRAP_CONTENT)
    }

    val velocity: Float
        get() = if (mBars.size > 0) {
            mBars[0].velocity
        } else {
            throw RuntimeException("No IndividualBar contained, the size of list is 0")
        }

    /**
     * Change the number of bars inside this view
     *
     * @param
     */
    fun changeBarCount(barCount: Int) {
        removeAllViews()
        init(color, invBetweenBars, barCount)
    }

    /**
     * Change color of the bar
     *
     * @param color color value
     */
    fun changeColor(color: Int) {
        removeAllViews()
        mBackgroundDrawable = -1
        init(color, invBetweenBars, barCount)
    }

    override fun removeAllViews() {
        shake(false)
        super.removeAllViews()
    }

    /**
     * change the interval between bars.
     *
     * @param inv
     */
    fun changeInvBetweenBars(inv: Int) {
        removeAllViews()
        init(color, inv, barCount)
    }

    fun changeDrawable(backgroundDrawable: Int) {
        removeAllViews()
        mBackgroundDrawable = backgroundDrawable
        init()
    }

    private fun applyBarDrawable(backgroundDrawable: Int) {
        for (bar in mBars) {
            bar.setBackgroundResource(backgroundDrawable)
        }
    }

    companion object {
        private const val VELOCITY_DEFAULT = 118.0f
    }

    init {
        // TODO Auto-generated constructor stub
        setupBarViewConfiguration(context, attrs)
        setupDefaultConfigurationIfNeed()
    }
}