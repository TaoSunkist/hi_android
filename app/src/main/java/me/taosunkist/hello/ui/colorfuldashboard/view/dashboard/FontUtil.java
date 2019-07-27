package me.taosunkist.hello.ui.colorfuldashboard.view.dashboard;

import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;

/**
 * Created by 94369 on 2016/4/13.
 */
public class FontUtil {
    /**
     * 获取字符高宽
     *
     * @param str
     * @return
     */
    public static int measureFontSize(float fontSize, String str, boolean isWidth) {
        Paint pen = new Paint();
        pen.setTextSize(fontSize);
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        pen.setTextSize(fontSize);
        Rect rect = new Rect();
        pen.getTextBounds(str, 0, str.length(), rect);
        return isWidth ? rect.width() : rect.height();
    }
}
