package me.taosunkist.hello.ui.debug

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.SizeMode
import me.taosunkist.hello.ui.BaseFragment

class DebugFragment : BaseFragment() {

    companion object {

        @JvmStatic
        fun newInstance() = DebugFragment().apply {}
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View = ComposeView(
        requireContext()
    ).apply {
            setContent {
                setPadding(5, 5, 5, 5)
                ParentLayout()
        }
    }

    @Composable
    @Preview
    fun ParentLayout() {

        FlowRow(
            mainAxisSize = SizeMode.Wrap,
            mainAxisAlignment = FlowMainAxisAlignment.Start,
            crossAxisSpacing = 5.dp,
            mainAxisSpacing = 5.dp
        ) {
            Button(onClick = {
            }, modifier = Modifier.wrapContentSize(align = Alignment.TopStart)) {
                Text("Button1")
            }
            Button(onClick = {
            }, modifier = Modifier.wrapContentSize(align = Alignment.TopStart)) {
                Text("Button2")
            }
            Button(onClick = {
            }, modifier = Modifier.wrapContentSize(align = Alignment.TopStart)) {
                Text("Button3")
            }
            Button(onClick = {
            }, modifier = Modifier.wrapContentSize(align = Alignment.TopStart)) {
                Text("Button4")
            }
            Button(onClick = {
            }, modifier = Modifier.wrapContentSize(align = Alignment.TopStart)) {
                Text("Button5")
            }
        }
    }
}
