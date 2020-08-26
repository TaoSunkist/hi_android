package me.taosunkist.hello.ui.frgment.countdowntimer.squareprogressbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.text.DecimalFormat;

import me.taosunkist.hello.Dimens;

public class SquareProgressView extends View {

    private float progress;
    private Paint progressBarPaint;
    private Paint outlinePaint;
    private Paint textPaint;

    private float widthInDp = 10;
    private float strokewidth = 0;
    private Canvas canvas;

    private boolean outline = false;
    private boolean startline = false;
    private boolean showProgress = false;
    private boolean centerline = false;

    private boolean roundedCorners = false;
    private float roundedCornersRadius = 10;

    private PercentStyle percentSettings = new PercentStyle(Align.CENTER, 150, true);
    private boolean clearOnHundred = false;
    private boolean isIndeterminate = false;
    private int indeterminate_count = 1;

    private float indeterminate_width = 20.0f;

    public SquareProgressView(Context context) {
        super(context);
        initializePaints(context);
    }

    public SquareProgressView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initializePaints(context);
    }

    public SquareProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializePaints(context);
    }

    private void initializePaints(Context context) {
        progressBarPaint = new Paint();
        progressBarPaint.setColor(context.getResources().getColor(android.R.color.holo_green_dark));
        progressBarPaint.setStrokeWidth(Dimens.INSTANCE.dpToPx((int) widthInDp));
        progressBarPaint.setAntiAlias(true);
        progressBarPaint.setStyle(Style.STROKE);

        outlinePaint = new Paint();
        outlinePaint.setColor(context.getResources().getColor(android.R.color.black));
        outlinePaint.setStrokeWidth(1);
        outlinePaint.setAntiAlias(true);
        outlinePaint.setStyle(Style.STROKE);

        textPaint = new Paint();
        textPaint.setColor(context.getResources().getColor(android.R.color.black));
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        this.canvas = canvas;
        super.onDraw(canvas);
        strokewidth = Dimens.INSTANCE.dpToPx((int) widthInDp);
        int cW = getWidth();
        int cH = getHeight();
        Log.i("taohui", "" + cW + "-" + cH + ", " + Dimens.INSTANCE.getScreenWidth() + "-" + Dimens.INSTANCE.getScreenHeight());
        float scope = (2 * cW) + (2 * cH) - (4 * strokewidth);
        float hSw = strokewidth / 2;

        if (isOutline()) {
            drawOutline();
        }

        if (isStartline()) {
            drawStartline();
        }

        if (isShowProgress()) {
            drawPercent(percentSettings);
        }

        if (isCenterline()) {
            drawCenterline(strokewidth);
        }

        if ((isClearOnHundred() && progress == 100.0) || (progress <= 0.0)) {
            return;
        }

        if (isIndeterminate()) {
            Path path = new Path();
            DrawStop drawEnd = getDrawEnd((scope / 100) * Float.valueOf(String.valueOf(indeterminate_count)), canvas);

            if (drawEnd.place == Place.TOP) {
                path.moveTo(drawEnd.location - indeterminate_width - strokewidth, hSw);
                path.lineTo(drawEnd.location, hSw);
                canvas.drawPath(path, progressBarPaint);
            }

            if (drawEnd.place == Place.RIGHT) {
                path.moveTo(cW - hSw, drawEnd.location - indeterminate_width);
                path.lineTo(cW - hSw, strokewidth + drawEnd.location);
                canvas.drawPath(path, progressBarPaint);
            }

            if (drawEnd.place == Place.BOTTOM) {
                path.moveTo(drawEnd.location - indeterminate_width - strokewidth, cH - hSw);
                path.lineTo(drawEnd.location, cH - hSw);
                canvas.drawPath(path, progressBarPaint);
            }

            if (drawEnd.place == Place.LEFT) {
                path.moveTo(hSw, drawEnd.location - indeterminate_width - strokewidth);
                path.lineTo(hSw, drawEnd.location);
                canvas.drawPath(path, progressBarPaint);
            }

            indeterminate_count++;
            if (indeterminate_count > 100) {
                indeterminate_count = 0;
            }
            invalidate();
        } else {
            Path path = new Path();
            DrawStop drawEnd = getDrawEnd((scope / 100) * Float.valueOf(String.valueOf(progress)), canvas);

            if (drawEnd.place == Place.TOP) {
                if (drawEnd.location > (cW / 2) && progress < 100.0) {
                    path.moveTo(cW / 2, hSw);
                    path.lineTo(drawEnd.location, hSw);
                } else {
                    path.moveTo(cW / 2, hSw);
                    path.lineTo(cW - hSw, hSw);
                    path.lineTo(cW - hSw, cH - hSw);
                    path.lineTo(hSw, cH - hSw);
                    path.lineTo(hSw, hSw);
                    path.lineTo(strokewidth, hSw);
                    path.lineTo(drawEnd.location, hSw);
                }
                canvas.drawPath(path, progressBarPaint);
            }

            if (drawEnd.place == Place.RIGHT) {
                path.moveTo(cW / 2, hSw);
                path.lineTo(cW - hSw, hSw);
                path.lineTo(cW - hSw, 0 + drawEnd.location);
                canvas.drawPath(path, progressBarPaint);
            }

            if (drawEnd.place == Place.BOTTOM) {
                path.moveTo(cW / 2, hSw);
                path.lineTo(cW - hSw, hSw);
                path.lineTo(cW - hSw, cH - hSw);
                path.lineTo(cW - strokewidth, cH - hSw);
                path.lineTo(drawEnd.location, cH - hSw);
                canvas.drawPath(path, progressBarPaint);
            }

            if (drawEnd.place == Place.LEFT) {
                path.moveTo(cW / 2, hSw);
                path.lineTo(cW - hSw, hSw);
                path.lineTo(cW - hSw, cH - hSw);
                path.lineTo(hSw, cH - hSw);
                path.lineTo(hSw, cH - strokewidth);
                path.lineTo(hSw, drawEnd.location);
                canvas.drawPath(path, progressBarPaint);
            }
        }
    }

    private void drawStartline() {
        Path outlinePath = new Path();
        outlinePath.moveTo(getWidth() / 2, 0);
        outlinePath.lineTo(getWidth() / 2, strokewidth);
        canvas.drawPath(outlinePath, outlinePaint);
    }

    private void drawOutline() {
        Path outlinePath = new Path();
        outlinePath.moveTo(0, 0);
        outlinePath.lineTo(getWidth(), 0);
        outlinePath.lineTo(getWidth(), getHeight());
        outlinePath.lineTo(0, getHeight());
        outlinePath.lineTo(0, 0);
        canvas.drawPath(outlinePath, outlinePaint);
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
        this.invalidate();
    }

    public void setColor(int color) {
        progressBarPaint.setColor(color);
        this.invalidate();
    }

    public void setWidthInDp(int width) {
        this.widthInDp = width;
        progressBarPaint.setStrokeWidth(Dimens.INSTANCE.dpToPx((int) widthInDp));
        this.invalidate();
    }

    public boolean isOutline() {
        return outline;
    }

    public void setOutline(boolean outline) {
        this.outline = outline;
        this.invalidate();
    }

    public boolean isStartline() {
        return startline;
    }

    public void setStartline(boolean startline) {
        this.startline = startline;
        this.invalidate();
    }

    private void drawPercent(PercentStyle setting) {
        textPaint.setTextAlign(setting.getAlign());
        if (setting.getTextSize() == 0) {
            textPaint.setTextSize((getHeight() / 10) * 4);
        } else {
            textPaint.setTextSize(setting.getTextSize());
        }

        String percentString = new DecimalFormat("###").format(getProgress());
        if (setting.isPercentSign()) {
            percentString = percentString + percentSettings.getCustomText();
        }

        textPaint.setColor(percentSettings.getTextColor());

        canvas.drawText(percentString, getWidth() / 2, (int) ((getHeight() / 2) - ((textPaint.descent() + textPaint.ascent()) / 2)), textPaint);
    }

    public boolean isShowProgress() {
        return showProgress;
    }

    public void setShowProgress(boolean showProgress) {
        this.showProgress = showProgress;
        this.invalidate();
    }

    public void setPercentStyle(PercentStyle percentSettings) {
        this.percentSettings = percentSettings;
        this.invalidate();
    }

    public PercentStyle getPercentStyle() {
        return percentSettings;
    }

    public void setClearOnHundred(boolean clearOnHundred) {
        this.clearOnHundred = clearOnHundred;
        this.invalidate();
    }

    public boolean isClearOnHundred() {
        return clearOnHundred;
    }

    private void drawCenterline(float strokewidth) {
        float centerOfStrokeWidth = strokewidth / 2;
        Path centerlinePath = new Path();
        centerlinePath.moveTo(centerOfStrokeWidth, centerOfStrokeWidth);
        centerlinePath.lineTo(getWidth() - centerOfStrokeWidth, centerOfStrokeWidth);
        centerlinePath.lineTo(getWidth() - centerOfStrokeWidth, getHeight() - centerOfStrokeWidth);
        centerlinePath.lineTo(centerOfStrokeWidth, getHeight() - centerOfStrokeWidth);
        centerlinePath.lineTo(centerOfStrokeWidth, centerOfStrokeWidth);
        canvas.drawPath(centerlinePath, outlinePaint);
    }

    public boolean isCenterline() {
        return centerline;
    }

    public void setCenterline(boolean centerline) {
        this.centerline = centerline;
        this.invalidate();
    }

    public boolean isIndeterminate() {
        return isIndeterminate;
    }

    public void setIndeterminate(boolean indeterminate) {
        isIndeterminate = indeterminate;
        this.invalidate();
    }

    public DrawStop getDrawEnd(float percent, Canvas canvas) {
        DrawStop drawStop = new DrawStop();
        strokewidth = Dimens.INSTANCE.dpToPx((int) widthInDp);
        float halfOfTheImage = getWidth() / 2;

        // top right
        if (percent > halfOfTheImage) {
            float second = percent - (halfOfTheImage);

            // right
            if (second > (getHeight() - strokewidth)) {
                float third = second - (getHeight() - strokewidth);

                // bottom
                if (third > (getWidth() - strokewidth)) {
                    float forth = third - (getWidth() - strokewidth);

                    // left
                    if (forth > (getHeight() - strokewidth)) {
                        float fifth = forth - (getHeight() - strokewidth);

                        // top left
                        if (fifth == halfOfTheImage) {
                            drawStop.place = Place.TOP;
                            drawStop.location = halfOfTheImage;
                        } else {
                            drawStop.place = Place.TOP;
                            drawStop.location = strokewidth + fifth;
                        }
                    } else {
                        drawStop.place = Place.LEFT;
                        drawStop.location = getHeight() - strokewidth - forth;
                    }

                } else {
                    drawStop.place = Place.BOTTOM;
                    drawStop.location = getWidth() - strokewidth - third;
                }
            } else {
                drawStop.place = Place.RIGHT;
                drawStop.location = strokewidth + second;
            }

        } else {
            drawStop.place = Place.TOP;
            drawStop.location = halfOfTheImage + percent;
        }

        return drawStop;
    }

    public void setRoundedCorners(boolean roundedCorners, float radius) {
        this.roundedCorners = roundedCorners;
        this.roundedCornersRadius = radius;
        if (roundedCorners) {
            progressBarPaint.setPathEffect(new CornerPathEffect(roundedCornersRadius));
        } else {
            progressBarPaint.setPathEffect(null);
        }
        this.invalidate();
    }

    public boolean isRoundedCorners() {
        return roundedCorners;
    }

    private class DrawStop {

        private Place place;
        private float location;

        public DrawStop() {

        }
    }

    public enum Place {
        TOP, RIGHT, BOTTOM, LEFT
    }
}
