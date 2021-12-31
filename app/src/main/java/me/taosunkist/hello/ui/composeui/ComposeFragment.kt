package me.taosunkist.hello.ui.composeui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mooveit.library.Fakeit
import me.taosunkist.hello.R
import me.taosunkist.hello.databinding.FragmentComposeBinding
import androidx.compose.foundation.lazy.items

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
            MaterialTheme {
                Conversation((0..10).map {
                    Fakeit.pokemon().name()
                })
            }
        }
    }

    @Composable
    fun Conversation(cardItems: List<String>) {
        LazyColumn {
            items(cardItems) { cardItem ->
                MessageCard(cardItem)
            }
        }
    }

    @Composable
    fun MessageCard(cardItem: String) {
        Row(modifier = Modifier.padding(8.dp)) {
            Image(
                painter = painterResource(R.drawable.default_head),
                contentDescription = "",
                modifier = Modifier.clip(CircleShape).size(64.dp, 64.dp)
            )

            Column {
                Text(
                    text = cardItem,
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

    }
}
