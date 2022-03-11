package me.taosunkist.hello.ui.debug

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import me.taosunkist.hello.ui.BaseFragment
import me.taosunkist.hello.ui.view.ComingUserFloatingBannerView

class DebugFragment : BaseFragment() {

    companion object {

        @JvmStatic
        fun newInstance() = DebugFragment().apply {}
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View = ComposeView(
        requireContext()
    ).apply {
        setContent {
            Button(onClick = {

            }) {
                Text(ComingUserFloatingBannerView::javaClass.name)
            }
        }
    }


}
