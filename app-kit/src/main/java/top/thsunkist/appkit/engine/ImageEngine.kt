package top.thsunkist.appkit.engine

import android.content.Context
import android.widget.ImageView

interface ImageEngine {


    /**
     * Loading image
     *
     * @param context
     * @param url
     * @param imageView
     */
    fun loadImage(context: Context, url: String, imageView: ImageView)

}