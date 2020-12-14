package top.thsunkist.brainhealthy.ui.reusable.viewcontroller

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.AnimRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleEventObserver
import com.gyf.immersionbar.ImmersionBar
import top.thsunkist.brainhealthy.R
import top.thsunkist.brainhealthy.ui.reusable.views.ProgressDialogWrapper

/*
Application（通过使用 @HiltAndroidApp）
Activity
Fragment
View
Service
BroadcastReceiver
如果您为某个 Fragment 添加注释，则还必须为使用该 Fragment 的所有 Activity 添加注释
@AndroidEntryPoint
*/
open class BaseActivity : AppCompatActivity() {

    open val viewRes: Int
        get() = R.layout.activity_base

    private lateinit var progressDialog: ProgressDialogWrapper

    private var disableBackButton: Boolean = false

    private val statusBarDefaultHeight = -1

    val container: ViewGroup
        get() = findViewById<FrameLayout>(R.id.activity_main_container)

    val controllerWindow: Window by lazy {
        val window = Window(activity = this)
        container.addView(window)
        window
    }

    /** 需要处理这个的有三个地方
     * 1. 融云的界面/ 2. 主页面预览/ 3. other ViewControllers
     * -1代表跟随父类一起处理
     * 0代表我们的ViewController自己处理,走 PresentationStyle#statusBarHeight */
    open var statusBarHeight: Int = statusBarDefaultHeight


    override fun onNewIntent(newIntent: Intent) {
        super.onNewIntent(newIntent)
        controllerWindow.controllers.forEach {
            it.onNewIntent(newIntent = newIntent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        ImmersionBar.with(this)
            .statusBarDarkFont(true)
            .init()

        if (statusBarHeight == -1) {
            statusBarHeight = ImmersionBar.getStatusBarHeight(this)
        }
        super.onCreate(savedInstanceState)
        setContentView(viewRes)
        lifecycle.addObserver(LifecycleEventObserver { _, _ -> })

        container.setBackgroundColor(Color.BLACK)
        progressDialog = ProgressDialogWrapper(parent = container)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }


    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        controllerWindow.onDestroy()
    }

    override fun onBackPressed() {
        if (disableBackButton) {
            return
        }

        /* TODO how to compatibility fragment */
        if (!controllerWindow.onBackPressed()) {
            super.onBackPressed()
        }
    }

    fun startActivity(
        intent: Intent,
        @AnimRes enterAnim: Int = R.anim.anim_slide_up,
        @AnimRes exitAnim: Int = R.anim.anim_none,
    ) {
        startActivity(intent)
        overridePendingTransition(enterAnim, exitAnim)
    }

    override fun finish() {
        overridePendingTransition(R.anim.anim_none, R.anim.anim_bottom_out)
        super.finish()
    }

    fun showProgressDialog(
        messageRes: Int,
        animated: Boolean = true,
        completion: (() -> Unit)? = null,
    ) {
        showProgressDialog(
            message = getString(messageRes),
            animated = animated,
            completion = completion
        )
    }

    fun showProgressDialog(
        message: String = "",
        animated: Boolean = true,
        completion: (() -> Unit)? = null,
    ) {
        disableBackButton = true
        progressDialog.progressText.text = message
        if (progressDialog.view.parent == null) {
            container.addView(progressDialog.view)
            progressDialog.show(animated = animated, completion = {
                completion?.invoke()
            })
        }
    }

    fun dismissProgressDialog(animated: Boolean = true, completion: (() -> Unit)? = null) {
        if (progressDialog.view.parent != null) {
            progressDialog.hide(animated = animated, completion = {
                container.removeView(progressDialog.view)
                completion?.invoke()
                disableBackButton = false
            })
        }
    }
}