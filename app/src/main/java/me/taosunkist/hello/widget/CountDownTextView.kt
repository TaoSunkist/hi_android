package me.taosunkist.hello.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView

class CountDownTextView : TextView {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
}