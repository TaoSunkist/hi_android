package me.taosunkist.hello.ui.colorfuldashboard.view.dashboard

import android.text.TextUtils

/**
 * Created by Sunkist on 2016/4/13.
 */
class Font {

    private lateinit var content: String
    var fontSize: Float = 0.toFloat()
        set(fontSize) {
            if (TextUtils.isEmpty(content)) {
                content = ""
            } else {
                width = FontUtil.measureFontSize(fontSize, content, true)
                height = FontUtil.measureFontSize(fontSize, content, false)
            }
            field = fontSize
        }
    var fontColor: Int = 0

    /**
     * 只有在设置了FontSize之后才能获得真正的width
     * (See[.setFontSize])
     *
     * @return
     */
    var width: Int = 0
    /**
     * 只有在设置了FontSize之后才能获得真正的height
     * (See[.setFontSize])
     *
     * @return
     */
    var height: Int = 0

    fun getContent(): String {
        return if (TextUtils.isEmpty(content)) {
            ""
        } else content
    }

    fun setContent(content: String) {
        this.content = content
    }
}
