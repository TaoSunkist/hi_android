package top.thsunkist.brainhealthy.ui.reusable.views

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class VerificationCodeButton : AppCompatButton {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    /* 开始发送验证码 */
    fun startCounting(countingCallback: () -> Unit): Disposable {
        return Observable.interval(1000, TimeUnit.MILLISECONDS)
            .take(60)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                val remain = 60 - it - 1
                val seconds = remain.toString()
                text = seconds
                if (remain == 0L) {
                }
            }
    }
}