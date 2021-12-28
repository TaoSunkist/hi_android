package me.taosunkist.hello.ui.composeui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.taosunkist.hello.databinding.FragmentComposeBinding

class ComposeFragment : Fragment() {

    private var _binding: FragmentComposeBinding? = null

    private val binding: FragmentComposeBinding get() = _binding!!

    companion object {

        @JvmStatic
        fun newInstance() = ComposeFragment().apply {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = FragmentComposeBinding.inflate(inflater, container, false).also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.composeView.setContent {
            MaterialTheme { initializeView() }
        }

    }

    @Preview
    @Composable
    private fun initializeView() {
        Text(
            text = System.currentTimeMillis().toString(),
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp).wrapContentHeight(Alignment.Bottom).clickable {
                Toast.makeText(requireContext(), "subtitle1", Toast.LENGTH_SHORT).show()
            },
        )

        Text(
            text = System.currentTimeMillis().toString(),
            style = MaterialTheme.typography.subtitle2,
            modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp).wrapContentHeight(Alignment.Bottom)
        )
    }
}
