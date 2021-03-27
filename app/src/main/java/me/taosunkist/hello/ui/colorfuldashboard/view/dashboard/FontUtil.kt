package me.taosunkist.hello.ui.colorfuldashboard.view.dashboard

import android.graphics.Paint
import android.graphics.Rect
import android.text.TextUtils

/**
 * Created by 94369 on 2016/4/13.
 */
object FontUtil {
    /**
     * 获取字符高宽
     *
     * @param str
     * @return
     */
    fun measureFontSize(fontSize: Float, str: String): Pair<Int, Int> {
        val pen = Paint()
        if (TextUtils.isEmpty(str)) {
            return Pair(0, 0)
        }
        pen.textSize = fontSize
        val rect = Rect()
        pen.getTextBounds(str, 0, str.length, rect)
        return Pair(rect.width(), rect.height())
    }
}
