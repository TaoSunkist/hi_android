package me.taosunkist.hello.ui.view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.view.marginStart
import me.taosunkist.hello.R
import top.thsunkist.appkit.utility.Dimens

class RoomLabelView : FrameLayout {

    val appCompatTextView = AppCompatTextView(context).apply {
        layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).also {
            it.setMargins(Dimens.marginXXLarge, 0, 0, 0)
        }
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context,
        attrs,
        defStyleAttr,
        defStyleRes)


    /*
    android:id="@+id/room_tag_text_view"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginStart="@dimen/marginPaddingPagInHotRoomItem"
                android:layout_marginTop="@dimen/marginPaddingPagInHotRoomItem"
                android:background="@drawable/shape_hot_room_tag_friend"
                android:includeFontPadding="false"
                android:paddingStart="@dimen/margin_large"
                android:paddingTop="@dimen/margin_xsmall"
                android:paddingEnd="@dimen/margin_large"
                android:paddingBottom="@dimen/margin_xsmall"
                android:textColor="@color/white"
                android:textSize="11sp"
                tools:text="N/A"
                */
    init {
        appCompatTextView.text = System.currentTimeMillis().toString()
        setBackgroundColor(ContextCompat.getColor(context, R.color.divideColor))
        addView(appCompatTextView)
    }

}