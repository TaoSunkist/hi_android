package me.taosunkist.uilib.dashboard;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import me.taosunkist.uilib.R;


/**
 * Created by Sunkist on 2016/4/8.
 * 自定义的信用分数仪表盘
 */
public class CreditScoresDashboard extends View {
    /**
     * 起点角度
     */
    private static final int ANGLE_START = 135;
    /**
     * 终点角度
     */
    private static final int ANGLE_END = 270;
    /**
     * 渐变色扇形条的旋转区域
     */
    private Matrix gradientScallopBarMatrix;
    /**
     * 仪表盘的全宽
     */
    private float mDashWidth;
    /**
     * 仪表盘的全高
     */
    private float mDashHeight;
    /**
     * 外部扇形条的颜色
     */
    private RectF outerArcProgressBarRectF;
    /**
     * 外部灰色背景弧形进度条的画笔
     */
    private Paint mOuterArcProgressBarPen;
    /**
     * 渐变色的绘制笔
     */
    private Paint mGradientScallopBarPen;

    private int[] mColors = {0xfffa6c01, 0xfff5a031, 0xff97c34d, 0xff51b76c, 0xff9ec449, 0xffafc741};
    private SweepGradient mSweepGradient;
    private float mInnerAcrProgressBarAllSize;
    private float mTickMarginBottom;
    private float mTickHeight;
    private float mTickWidth;
    private Paint mTickFontPen;
    private float mOuterBarStrokeWidth;
    private float tickFontSize;
    private float mTickFontMargin;
    private Font mTickFont;
    private RectF gradientScallopBarRectF;
    private float progressSweep;
    private int tickFontColor;

    public CreditScoresDashboard(Context context) {
        this(context, null);
    }

    public CreditScoresDashboard(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CreditScoresDashboard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupParams();
        if (attrs != null) {
            parseStyleable(context, attrs);
        }
    }


    private void setupParams() {
        this.mGradientScallopBarPen = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.mOuterArcProgressBarPen = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.mTickFontPen = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.mTickFont = new Font();
        this.outerArcProgressBarRectF = new RectF();
        this.mTickRectF = new RectF();
        this.gradientScallopBarRectF = new RectF();
        this.gradientScallopBarMatrix = new Matrix();

    }


    /**
     * 获取XML配置参数并初始化默认值
     *
     * @param context
     * @param attrs
     */
    private void parseStyleable(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.credit_scores_dashboard);

        mTickHeight = typedArray.getDimension(R.styleable.credit_scores_dashboard_tick_height, 0);
        mTickWidth = typedArray.getDimension(R.styleable.credit_scores_dashboard_tick_width, 0);
        mTickFontMargin = typedArray.getDimension(R.styleable.credit_scores_dashboard_tick_font_margin, 0);
        tickFontSize = typedArray.getDimension(R.styleable.credit_scores_dashboard_tick_font_size, 0);
        //获得刻度的上下距离
        mTickMarginBottom = typedArray.getDimension(R.styleable.credit_scores_dashboard_tick_margin_bottom, 0);
        //获得设置的内部的滚动Bar的宽度
        mInnerAcrProgressBarAllSize = typedArray.getDimension(R.styleable.credit_scores_dashboard_inner_arc_progress_bar_width, 0);
        tickFontColor = typedArray.getColor(R.styleable.credit_scores_dashboard_tick_font_color, Color.TRANSPARENT);
        //通过内部的弧形进度控制外部刻度的大小，计算规则为：内部弧形滚动Bar的Size+tick的Size+刻度间距
        typedArray.recycle();
        //全宽=内部扇形进度条的宽度+刻度的高度+刻度的顶部间距+刻度的底部间距+30dp
        mTickFont.width = FontUtil.measureFontSize(tickFontSize, String.valueOf(maxScores), true);
        mTickFont.height = FontUtil.measureFontSize(tickFontSize, String.valueOf(maxScores), false);
        this.mDashWidth = mInnerAcrProgressBarAllSize
                + mTickMarginBottom * 2
                + mTickHeight * 2
                + mTickFontMargin * 2
                + mTickFont.width * 2;

        mDashHeight = mDashWidth;
        this.mOuterBarStrokeWidth = mTickHeight + mTickMarginBottom + mTickMarginBottom / 2;
        //刻度的上下边距和高宽
        mTickRectF.left = mDashWidth / 2 - mTickWidth;
        mTickRectF.right = mDashWidth / 2 + mTickWidth;
        mTickRectF.top = mTickFont.width + mTickFontMargin;
        mTickRectF.bottom = mTickRectF.top + mTickHeight;

        //计算隐藏的Bar的区域
        this.outerArcProgressBarRectF.set(
                mTickFont.width + mTickFontMargin + mTickMarginBottom
                , mTickFont.width + mTickFontMargin + mTickMarginBottom
                , mDashWidth - mTickFont.width - mTickFontMargin - mTickMarginBottom
                , mDashHeight - mTickFont.width - mTickFontMargin - mTickMarginBottom
        );

        this.gradientScallopBarRectF.set(
                mTickFont.width
                , mTickFont.width
                , mDashWidth - mTickFont.width
                , mDashWidth - mTickFont.width);

        this.mSweepGradient = new SweepGradient(
                this.mDashWidth / 2
                , this.mDashWidth / 2
                , mColors, null);

        this.gradientScallopBarMatrix.setRotate(127
                , this.mDashWidth / 2
                , this.mDashWidth / 2);

        this.mSweepGradient.setLocalMatrix(this.gradientScallopBarMatrix);

        this.mOuterArcProgressBarPen.setStyle(Paint.Style.STROKE);
        this.mOuterArcProgressBarPen.setColor(Color.rgb(33, 99, 0xff));
        mOuterArcProgressBarPen.setStrokeWidth(mOuterBarStrokeWidth);
        this.mTickFontPen.setTextSize(tickFontSize);
        this.mTickFontPen.setColor(tickFontColor);
        this.mGradientScallopBarPen.setShader(this.mSweepGradient);
    }

    private RectF mTickRectF;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(
                (int) this.mDashWidth
                , (int) this.mDashHeight);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //是否已经设置渐变色背景色
        canvas.drawArc(this.gradientScallopBarRectF, ANGLE_START, ANGLE_END, false, this.mGradientScallopBarPen);
        canvas.drawArc(outerArcProgressBarRectF, ANGLE_START - 2 + progressSweep, ANGLE_END + 4 - progressSweep, false, mOuterArcProgressBarPen);
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        AtomicInteger atomicInteger = new AtomicInteger(9);
        canvas.save();
        for (int i = 0; i < 31; i++) {
            if (i < 15) {
                atomicInteger.addAndGet(9);
                canvas.rotate(-9, mDashWidth / 2, mDashHeight / 2);
            } else {
                if (atomicBoolean.getAndSet(false)) {
                    canvas.rotate(atomicInteger.get() - 9, mDashWidth / 2, mDashHeight / 2);
                } else {
                    canvas.rotate(9, mDashWidth / 2, mDashHeight / 2);
                }
            }
            canvas.clipRect(0, 0, mDashWidth, mDashHeight);
            canvas.clipRect(mTickRectF, Region.Op.DIFFERENCE);
        }
        canvas.drawColor(Color.WHITE);
        canvas.restore();

        float x, y;
        x = (float) (((mDashWidth) / 2) + mDashWidth / 2 * Math.cos(135 * Math.PI / 180));
        y = (float) (((mDashWidth) / 2) + mDashWidth / 2 * Math.sin(135 * Math.PI / 180));
        canvas.drawText(String.valueOf(minScores)
                , x - mTickFont.width + mTickFont.width / 2
                , y - mTickFont.height
                , mTickFontPen);

        x = (float) (((mDashWidth) / 2) + mDashWidth / 2 * Math.cos(189 * Math.PI / 180));
        y = (float) (((mDashWidth) / 2) + mDashWidth / 2 * Math.sin(189 * Math.PI / 180));
        canvas.drawText(scoresRange.size() != 0 ? scoresRange.get(0) : ""
                , x
                , y
                , mTickFontPen);

        x = (float) (((mDashWidth) / 2) + mDashWidth / 2 * Math.cos(234 * Math.PI / 180));
        y = (float) (((mDashWidth) / 2) + mDashWidth / 2 * Math.sin(234 * Math.PI / 180));
        canvas.drawText(scoresRange.size() != 0 ? scoresRange.get(1) : ""
                , x
                , y
                , mTickFontPen);

        x = (float) (((mDashWidth) / 2) + mDashWidth / 2 * Math.cos(306 * Math.PI / 180));
        y = (float) (((mDashWidth) / 2) + mDashWidth / 2 * Math.sin(306 * Math.PI / 180));
        canvas.drawText(scoresRange.size() != 0 ? scoresRange.get(2) : ""
                , x - mTickFont.width
                , y
                , mTickFontPen);

        x = (float) (((mDashWidth) / 2) + mDashWidth / 2 * Math.cos(351 * Math.PI / 180));
        y = (float) (((mDashWidth) / 2) + mDashWidth / 2 * Math.sin(351 * Math.PI / 180));
        canvas.drawText(scoresRange.size() != 0 ? scoresRange.get(3) : ""
                , x - mTickFont.width
                , y
                , mTickFontPen);

        x = (float) (((mDashWidth) / 2) + mDashWidth / 2 * Math.cos(405 * Math.PI / 180));
        y = (float) (((mDashWidth) / 2) + mDashWidth / 2 * Math.sin(405 * Math.PI / 180));
        canvas.drawText(String.valueOf(maxScores)
                , x - mTickFont.width + mTickFont.width / 2
                , y - mTickFont.height
                , mTickFontPen);
    }

    public void setProgressValue(float progressValue, float progressMaxValue) {
        this.progressSweep = progressValue / progressMaxValue * (ANGLE_END + 4);
        invalidate();
    }

    /**
     * 信用分的最大值，默认300-900
     */
    private int minScores = 0, maxScores = 100;
    private ArrayList<String> scoresRange = new ArrayList<>(4);


    private int mProgress;

    public void setProgress(int progress) {
        this.mProgress = progress;
        invalidate();
    }

    public void setMaxProgress(int maxProgrexx) {
        this.maxScores = maxProgrexx;
    }
}












