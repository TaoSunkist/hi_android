package me.taosunkist.uilib.dashboard;

import android.text.TextUtils;

/**
 * Created by Sunkist on 2016/4/13.
 */
public class Font {

    private String content;
    private float fontSize;
    private int fontColor;

    public int width;
    public int height;

    public String getContent() {
        if (TextUtils.isEmpty(content)) {
            return "";
        }
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public float getFontSize() {
        return fontSize;
    }

    public void setFontSize(float fontSize) {
        if (TextUtils.isEmpty(content)) {
            content = "";
        } else {
            width = FontUtil.measureFontSize(fontSize, content, true);
            height = FontUtil.measureFontSize(fontSize, content, false);
        }
        this.fontSize = fontSize;
    }

    public int getFontColor() {
        return fontColor;
    }

    public void setFontColor(int fontColor) {
        this.fontColor = fontColor;
    }

    /**
     * 只有在设置了FontSize之后才能获得真正的width
     * (See{@link #setFontSize})
     *
     * @return
     */
    public int getWidth() {
        return width;
    }

    /**
     * 只有在设置了FontSize之后才能获得真正的height
     * (See{@link #setFontSize})
     *
     * @return
     */
    public int getHeight() {
        return height;
    }

    /**
     * 清除属性
     */
    public void reset() {
        this.width = 0;
        this.height = 0;
    }
}
