package me.taosunkist.hello.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import me.taosunkist.hello.R
import me.taosunkist.hello.databinding.FragmentGorillaWebSocketBinding
import me.taosunkist.hello.utility.printf
import okhttp3.*
import okio.ByteString

class GorillaWebSocketFragment : Fragment() {

	companion object {
		private const val ARG_PARAM1 = "param1"
		private const val ARG_PARAM2 = "param2"

		fun newInstance(param1: String = "", param2: String = ""): GorillaWebSocketFragment {
			val fragment = GorillaWebSocketFragment()
			val args = Bundle()
			args.putString(ARG_PARAM1, param1)
			args.putString(ARG_PARAM2, param2)
			fragment.arguments = args
			return fragment
		}
	}

	private var mParam1: String? = null
	private var mParam2: String? = null

	private lateinit var binding: FragmentGorillaWebSocketBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		if (arguments != null) {
			mParam1 = arguments!!.getString(ARG_PARAM1)
			mParam2 = arguments!!.getString(ARG_PARAM2)
		}
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return FragmentGorillaWebSocketBinding.inflate(inflater, container, false).apply {
			binding = this
		}.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		binding.connectWsHostImageView.setOnClickListener { connectWsHostImageViewPressed() }
	}

	private fun connectWsHostImageViewPressed() {
		binding.connectWsHostImageView.isSelected = binding.connectWsHostImageView.isSelected.not()

		val request = Request.Builder().url(binding.wesocketUrlEditText.text.toString()).build()
		val webSocket = OkHttpClient().newWebSocket(request, object : WebSocketListener() {

			override fun onOpen(webSocket: WebSocket, response: Response) {
				activity?.runOnUiThread {
					binding.loggerRecyclerView.logI("onOpen(webSocket: WebSocket, response: Response)")
				}
			}

			override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
				activity?.runOnUiThread {
					binding.loggerRecyclerView.logI("onFailure($webSocket: WebSocket, $t: Throwable, $response: Response?)")
				}
			}

			override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
				activity?.runOnUiThread {
					binding.loggerRecyclerView.logI("onClosing(webSocket: WebSocket, code: Int, reason: String)")
				}
			}

			override fun onMessage(webSocket: WebSocket, text: String) {
				activity?.runOnUiThread {
					binding.loggerRecyclerView.logI("onMessage(webSocket: WebSocket, text: String)")
				}
			}

			override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
				activity?.runOnUiThread {
					binding.loggerRecyclerView.logI("onMessage(webSocket: WebSocket, bytes: ByteString)")
				}
			}

			override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
				activity?.runOnUiThread {
					binding.loggerRecyclerView.logI("onClosed(webSocket: WebSocket, code: Int, reason: String)")
				}
			}
		})
	}
}