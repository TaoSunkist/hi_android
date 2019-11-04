package me.taosunkist.hello.ui.watermark;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import me.taosunkist.hello.R;

public class TTest {

  public static Bitmap addWaterMark(Activity activity, Bitmap src) {
    int w = src.getWidth();
    int h = src.getHeight();
    Bitmap result = Bitmap.createBitmap(w, h, src.getConfig());
    Canvas canvas = new Canvas(result);
    canvas.drawBitmap(src, 0, 0, null);

    int watermarkPadding = 50;
    Bitmap waterMark =
        BitmapFactory.decodeResource(activity.getResources(), R.drawable.tatame_watermark);
    int newWatermarkWidth = w / 3;
    int newWatermarkHeight = (waterMark.getHeight() * newWatermarkWidth) / waterMark.getWidth();
    Bitmap newWatermark =
        Bitmap.createScaledBitmap(waterMark, newWatermarkWidth, newWatermarkHeight, true);
    canvas.drawBitmap(
        newWatermark,
        w - newWatermarkWidth - watermarkPadding,
        h - newWatermarkHeight - watermarkPadding,
        null);

    return result;
  }
}
