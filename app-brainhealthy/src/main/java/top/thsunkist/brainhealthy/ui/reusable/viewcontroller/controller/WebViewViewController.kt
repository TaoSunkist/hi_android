package top.thsunkist.brainhealthy.ui.reusable.viewcontroller.controller

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.gyf.immersionbar.ImmersionBar
import top.thsunkist.brainhealthy.R
import top.thsunkist.brainhealthy.databinding.ViewControllerWebViewBinding

open class WebViewViewController : BaseViewController() {


    lateinit var binding: ViewControllerWebViewBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return ViewControllerWebViewBinding.inflate(
            inflater, container, false
        ).apply {
            binding = this
        }.root
    }

    override fun viewDidLoad(view: View) {
        super.viewDidLoad(view)
        ImmersionBar.setTitleBar(requireActivity, binding.toolbar)
        setupToolbar(view)
        binding.fragmentAgreementWebview.webViewClient = WebViewClient()
    }

    private fun setupToolbar(view: View) {
        view.findViewById<View>(R.id.back_image_view).also { dismiss(animated = true) }
    }
}