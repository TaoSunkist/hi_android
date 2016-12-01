package me.taosunkist.uilib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * TODO: document your custom view class.
 * 波形水纹动态控件
 *
 * @author Sunkis
 */
public class WaveView extends View {
    private int mExampleColor = Color.RED; // TODO: use a default from R.color...
    private float mExampleDimension = 0; // TODO: use a default from R.dimen...
    private int mViewWidth;
    private int mViewHeight;

    private TextPaint mTextPaint;
    /**
     * 绘制Path的画笔
     */
    private Paint mPathPen;

    public WaveView(Context context) {
        super(context);
        init(null, 0);
    }

    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public WaveView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mViewWidth = MeasureSpec.getSize(widthMeasureSpec);
        mViewHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(mViewWidth, mViewHeight);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.WaveView, defStyle, 0);

        mExampleColor = a.getColor(R.styleable.WaveView_exampleColor, mExampleColor);
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        mExampleDimension = a.getDimension(R.styleable.WaveView_exampleDimension, mExampleDimension);

        a.recycle();

        // Set up a default TextPaint object
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        // Update TextPaint and text measurements from attributes
        mTextPaint.setTextSize(mExampleDimension);
        mTextPaint.setColor(mExampleColor);

        mPathPen = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPathPen.setColor(mExampleColor);
        mPathPen.setStyle(Paint.Style.STROKE);
        mPathPen.setStrokeWidth(2);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;

        drawPath(canvas);
    }

    /**
     * 绘制路径
     *
     * @param canvas
     */
    private void drawPath(Canvas canvas) {
        canvas.translate(mViewWidth / 2, mViewHeight / 2);
        Path path = new Path();
        path.lineTo(0, 0);
        path.lineTo(45, 45);
        path.quadTo(0, 0, 100, 100);
        path.quadTo(122, 122, 222, 222);
        canvas.drawPath(path, mPathPen);
    }

    public void run() {

    }
}
