package me.taosunkist.uilib.dashboard;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import my.sample.android.uikit.R;
import my.sample.android.uikit.tools.ResourcesUtil;

/**
 * Created by Sunkist on 2016/4/12.
 * 弧形进度条
 */
public class ArcProgressBar extends View {
    /**
     * 弧形进度的区域
     */
    private RectF innerArcBarRectF;
    /**
     * 起点角度
     */
    private int startAngel;
    /**
     * 终点角度
     */
    private int endAngel;
    /**
     * 弧形条的傻笔
     */
    private Paint arcBarPen;
    /**
     * 弧形进度的宽度
     */
    private int strokeWidth;
    /**
     * 最大进度值
     */
    private float allSize;
    private Paint centerFontPen;
    private Paint bottomFontPen;
    private Paint belowFontPen;
    private float[] mPts;

    public ArcProgressBar(Context context) {
        this(context, null);
    }

    public ArcProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupParams();
        if (attrs != null) {
            parseAttrs(context, attrs);
        }
    }

    private void parseAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.arc_progress_bar);
        this.startAngel = typedArray.getInteger(R.styleable.arc_progress_bar_angel_start, 0);
        this.endAngel = typedArray.getInteger(R.styleable.arc_progress_bar_angel_end, 360);
        this.strokeWidth = (int) typedArray.getDimension(R.styleable.arc_progress_bar_stroke_width, 0);

        float centerFontSize = typedArray.getDimension(R.styleable.arc_progress_bar_center_font_size, 0);
        float belowFontSize = typedArray.getDimension(R.styleable.arc_progress_bar_below_font_size, 0);
        float bottomFontSize = typedArray.getDimension(R.styleable.arc_progress_bar_bottom_font_size, 0);

        int centerFontColor = typedArray.getColor(R.styleable.arc_progress_bar_center_font_color, Color.TRANSPARENT);
        int belowFontColor = typedArray.getColor(R.styleable.arc_progress_bar_below_font_color, Color.TRANSPARENT);
        int bottomFontColor = typedArray.getColor(R.styleable.arc_progress_bar_bottom_font_color, Color.TRANSPARENT);
        allSize = typedArray.getDimension(R.styleable.arc_progress_bar_all_size, 0);
        typedArray.recycle();

        innerArcBarRectF.set(0 + strokeWidth / 2, 0 + strokeWidth / 2, allSize - strokeWidth / 2, allSize - strokeWidth / 2);

        centerFont.setFontSize(centerFontSize);
        centerFont.setFontColor(centerFontColor);

        belowFont.setFontSize(belowFontSize);
        belowFont.setFontColor(belowFontColor);

        bottomFont.setFontSize(bottomFontSize);
        bottomFont.setFontColor(bottomFontColor);

        centerFontPen.setTextSize(centerFont.getFontSize());
        bottomFontPen.setTextSize(bottomFont.getFontSize());
        belowFontPen.setTextSize(belowFont.getFontSize());

        mPts = new float[]{
                allSize / 2 - getDpValue(16), allSize / 2 - getDpValue(14),
                allSize / 2 - getDpValue(51), allSize / 2 - getDpValue(14),
                allSize / 2 + getDpValue(16), allSize / 2 - getDpValue(14),
                allSize / 2 + getDpValue(51), allSize / 2 - getDpValue(14)
        };

    }

    /**
     * 装载参数
     */
    public void setupParams() {
        this.innerArcBarRectF = new RectF();
        this.arcBarPen = new Paint(Paint.ANTI_ALIAS_FLAG);
        centerFontPen = new Paint(Paint.ANTI_ALIAS_FLAG);
        bottomFontPen = new Paint(Paint.ANTI_ALIAS_FLAG);
        belowFontPen = new Paint(Paint.ANTI_ALIAS_FLAG);

        centerFontPen.setTextAlign(Paint.Align.CENTER);
        bottomFontPen.setTextAlign(Paint.Align.CENTER);
        belowFontPen.setTextAlign(Paint.Align.CENTER);

        centerFont = new Font();
        belowFont = new Font();
        bottomFont = new Font();
        //测试数据
        centerFont.setContent(String.valueOf(0));
        belowFont.setContent("获取中");
        bottomFont.setContent("获取中");
    }

    /**
     * 正中心字体的尺寸
     */
    private Font centerFont;
    private Font belowFont;
    private Font bottomFont;
    private int mArcProgressBarColor;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.arcBarPen.setStyle(Paint.Style.STROKE);
        this.arcBarPen.setColor(Color.BLUE);
        this.arcBarPen.setStrokeCap(Paint.Cap.ROUND);
        this.arcBarPen.setStrokeWidth(this.strokeWidth);
        canvas.drawArc(innerArcBarRectF, startAngel, progressSweep, false, arcBarPen);
        if (!TextUtils.isEmpty(centerFont.getContent())) {
            canvas.drawText(centerFont.getContent(), 0, centerFont.getContent().length()
                    , this.allSize / 2
                    , this.allSize / 2 + centerFont.getHeight() / 2 - belowFont.getHeight()
                    , centerFontPen);
        } else {
            centerFontPen.setStrokeWidth(centerFont.getFontSize() / 13f);
            canvas.drawLines(mPts, centerFontPen);
        }

        canvas.drawText(belowFont.getContent(), 0, belowFont.getContent().length()
                , this.allSize / 2
                , this.allSize / 2 + centerFont.getHeight()
                , belowFontPen);

        canvas.drawText(bottomFont.getContent(), 0, bottomFont.getContent().length()
                , this.allSize / 2
                , this.allSize - bottomFont.getHeight() * 2.3f
                , bottomFontPen);

        canvas.drawColor(Color.TRANSPARENT);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension((int) allSize, (int) allSize);
    }


    /**
     * 进度值
     */
    private float progressValue = 0;
    /**
     * 进度角度
     */
    private int progressSweep;
    private int progressMaxValue = 100;

    private int getDpValue(int w) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, w, getContext().getResources().getDisplayMetrics());
    }

    public void setProgress(int progress) {
        this.progressValue = progress;
        float ratio = progressValue / progressMaxValue;
        progressSweep = (int) (ratio * endAngel);
        Log.d("ProgressBarValue", "" + progressSweep);
        this.invalidate();
    }

    public void setMaxProgress(int maxProgress) {
        this.progressMaxValue = maxProgress;
    }
}
