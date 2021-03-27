package me.taosunkist.hello.ui.squareprogressbar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import androidx.annotation.ColorInt;

import me.taosunkist.hello.R;
import top.thsunkist.library.ui.resuable.rectangleprogressbar.PercentStyle;
import top.thsunkist.library.ui.resuable.rectangleprogressbar.SquareProgressView;
import top.thsunkist.library.utilities.Dimens;

public class SquareProgressBar extends RelativeLayout {

    private ImageView imageView;
    private SquareProgressView bar;
    private boolean opacity = false;
    private boolean greyscale;
    private boolean isFadingOnProgress = false;
    private boolean roundedCorners = false;

    public SquareProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context, R.id.squareProgressBar1);
    }

    public SquareProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, R.id.squareProgressBar1);
    }

    public SquareProgressBar(Context context) {
        super(context);
        initialize(context, R.id.squareProgressBar1);
    }

    public void setImage(int image) {
        imageView.setImageResource(image);

    }

    private void initialize(Context context, int p) {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.view_square_progress_bar, this, true);
        bar = findViewById(p);
        imageView = findViewById(R.id.imageView1);
        bar.bringToFront();
    }

    public void setImageDrawable(Drawable imageDrawable) {
        imageView.setImageDrawable(imageDrawable);
    }


    public void setImageScaleType(ScaleType scale) {
        imageView.setScaleType(scale);
    }

    public void setProgress(float progress) {
        bar.setProgress(progress);
        if (opacity) {
            if (isFadingOnProgress) {
                setOpacity(100 - (int) progress);
            } else {
                setOpacity((int) progress);
            }
        } else {
            setOpacity(100);
        }
    }

    public void setHoloColor(int androidHoloColor) {
        bar.setColor(getContext().getResources().getColor(androidHoloColor));
    }

    public void setColor(String colorString) {
        bar.setColor(Color.parseColor(colorString));
    }

    public void setColor(@ColorInt int colorInt) {
        bar.setColor(colorInt);
    }

    public void setColorRGB(int r, int g, int b) {
        bar.setColor(Color.rgb(r, g, b));
    }

    public void setColorRGB(int rgb) {
        bar.setColor(rgb);
    }

    public void setWidth(int width) {
        int padding = Dimens.INSTANCE.dpToPx(width);
        imageView.setPadding(padding, padding, padding, padding);
        bar.setWidthInDp(width);
    }

    private void setOpacity(int progress) {
        imageView.setAlpha((int) (2.55 * progress));
    }

    public void setOpacity(boolean opacity) {
        this.opacity = opacity;
        setProgress(bar.getProgress());
    }

    public void setOpacity(boolean opacity, boolean isFadingOnProgress) {
        this.opacity = opacity;
        this.isFadingOnProgress = isFadingOnProgress;
        setProgress(bar.getProgress());
    }

    public void setImageGrayscale(boolean greyscale) {
        this.greyscale = greyscale;
        if (greyscale) {
            ColorMatrix matrix = new ColorMatrix();
            matrix.setSaturation(0);
            imageView.setColorFilter(new ColorMatrixColorFilter(matrix));
        } else {
            imageView.setColorFilter(null);
        }
    }

    public boolean isOpacity() {
        return opacity;
    }

    public boolean isGreyscale() {
        return greyscale;
    }

    public void drawOutline(boolean drawOutline) {
        bar.setOutline(drawOutline);
    }

    public boolean isOutline() {
        return bar.isOutline();
    }

    public void drawStartline(boolean drawStartline) {
        bar.setStartline(drawStartline);
    }

    public boolean isStartline() {
        return bar.isStartline();
    }

    public void showProgress(boolean showProgress) {
        bar.setShowProgress(showProgress);
    }

    public boolean isShowProgress() {
        return bar.isShowProgress();
    }

    public void setPercentStyle(PercentStyle percentStyle) {
        bar.setPercentStyle(percentStyle);
    }

    public PercentStyle getPercentStyle() {
        return bar.getPercentStyle();
    }

    public void setClearOnHundred(boolean clearOnHundred) {
        bar.setClearOnHundred(clearOnHundred);
    }

    public boolean isClearOnHundred() {
        return bar.isClearOnHundred();
    }

    public void setImageBitmap(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }

    public void setIndeterminate(boolean indeterminate) {
        bar.setIndeterminate(indeterminate);
    }

    public boolean isIndeterminate() {
        return bar.isIndeterminate();
    }

    public void drawCenterline(boolean drawCenterline) {
        bar.setCenterline(drawCenterline);
    }

    public boolean isCenterline() {
        return bar.isCenterline();
    }

    public ImageView getImageView() {
        return imageView;
    }

    public double getProgress() {
        return bar.getProgress();
    }

    public void setProgress(int progress) {
        bar.setProgress(progress);
    }

    public void setRoundedCorners(boolean useRoundedCorners) {
        bar.setRoundedCorners(useRoundedCorners, 10);
    }

    public void setRoundedCorners(boolean useRoundedCorners, float radius) {
        bar.setRoundedCorners(useRoundedCorners, radius);
    }


    public boolean isRoundedCorners() {
        return bar.isRoundedCorners();
    }
}
