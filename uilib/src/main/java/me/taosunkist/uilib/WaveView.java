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
import android.util.Log;
import android.view.View;

/**
 * TODO: document your custom view class.
 * 波形水纹动态控件
 *
 * @author Sunkis
 */
public class WaveView extends View implements Runnable {
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

    int x1, y1, x2, y2;

    /**
     * 绘制路径
     *
     * @param canvas
     */
    private void drawPath(Canvas canvas) {
        drawCartesianCoordinates(canvas);
        mPathPen.setColor(Color.BLACK);
        canvas.translate(mViewWidth / 2, mViewHeight / 2);
        Path path = new Path();
        path.quadTo(200, 400, 600, 800);
        canvas.drawPath(path, mPathPen);
    }

    /**
     * 绘制平面坐标系
     *
     * @param canvas
     */
    private void drawCartesianCoordinates(Canvas canvas) {
        mPathPen.setColor(Color.YELLOW);
        Path yPath = new Path();
        yPath.moveTo(mViewWidth / 2, 0);
        yPath.lineTo(mViewWidth / 2, mViewHeight);
        canvas.drawPath(yPath, mPathPen);
        Path xPath = new Path();
        xPath.moveTo(0, mViewHeight / 2);
        xPath.lineTo(mViewWidth, mViewHeight / 2);
        canvas.drawPath(xPath, mPathPen);
    }

    private Handler handler = new Handler();

    @Override
    public void run() {
        x2--;
        y2++;
        Log.d("WaveView", y2 + "");
        invalidate();
        if (x2 >= 1000) {
            handler.removeCallbacks(this);
        } else {
            handler.postDelayed(this, 10);
        }
    }
}
