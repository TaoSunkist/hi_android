package me.taosunkist.hello.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.common.io.ByteArrayDataInput
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.messageadapter.protobuf.ProtobufMessageAdapter
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import me.taosunkist.hello.ProtobufferUtility
import me.taosunkist.hello.UserWSService
import me.taosunkist.hello.databinding.FragmentGorillaWebSocketBinding
import me.taosunkist.hello.hilo.UserProxy
import me.taosunkist.hello.utility.printf
import okhttp3.*
import okio.ByteString
import java.io.ByteArrayOutputStream
import java.io.DataInputStream
import java.math.BigInteger
import java.util.concurrent.TimeUnit

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
			mParam1 = requireArguments().getString(ARG_PARAM1)
			mParam2 = requireArguments().getString(ARG_PARAM2)
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

		val heartBeat = UserProxy.heartBeat.newBuilder().setToken(System.currentTimeMillis().toString()).build()
		binding.loggerRecyclerView.logI("${heartBeat.toByteArray().asList()}")

	}

	private fun connectWsHostImageViewPressed() {
		binding.connectWsHostImageView.isSelected = binding.connectWsHostImageView.isSelected.not()

//		val scarlet = Scarlet.Builder()
//			.webSocketFactory(okHttpClient.newWebSocketFactory("ws://192.168.1.180:8080/ws"))
//			.addMessageAdapterFactory(ProtobufMessageAdapter.Factory())
//			.build()
		val heartBeat = UserProxy.heartBeat.newBuilder().setToken(System.currentTimeMillis().toString()).build()
		/* 2 uint16 */
		val version: Short = 10
		/* 4 u32 */
		val msgType: Int = 100
		/* 8 u64 */
		val msgID: Long = 1000
		/* 8 u64 */
		val timestamp: Long = 10000
		/* 4 u32 */
		val dataLen: Int = heartBeat.toByteArray().size
		/* data bytes of array */
		val data = heartBeat.toByteArray()
		/* 4 u32 */
		val checkSum: Long = 99999

		val versionBytes = ProtobufferUtility.getBytes<Short>(version)
		val msgTypeBytes = ProtobufferUtility.getBytes<Int>(msgType)
		val msgIdBytes = ProtobufferUtility.getBytes<Long>(msgID)
		val timestampBytes = ProtobufferUtility.getBytes<Long>(timestamp)
		val dataLenBytes = ProtobufferUtility.getBytes<Int>(dataLen)
		val dataBytes = heartBeat.toByteArray()
		val checkSumBytes = ProtobufferUtility.getBytes<Long>(checkSum)

		val badi = ByteArrayOutputStream()
		badi.write(versionBytes)
		badi.write(msgTypeBytes)
		badi.write(msgIdBytes)
		badi.write(timestampBytes)
		badi.write(dataLenBytes)
		badi.write(dataBytes)
		badi.write(checkSumBytes)
//		binding.loggerRecyclerView.logI("${badi.toByteArray().asList()}")
//		val userWSService = scarlet.create(UserWSService::class.java)
//		printf("sendBytes result: " + userWSService.sendBytes(badi.toByteArray()))

		val okHttpClient = OkHttpClient().newBuilder().build()
		val request: Request = Request.Builder()
			.get()
			.url("ws://192.168.1.180:8080/ws")
			.build()

		okHttpClient.newWebSocket(request, object : WebSocketListener() {
			override fun onOpen(webSocket: WebSocket, response: Response) {
				val mWebSocket = webSocket
				printf("client onOpen")
				printf("client request header:" + response.request().headers())
				printf("client response header:" + response.headers())
				printf("client response:$response")
				printf("client $mWebSocket")
			}

			override fun onMessage(webSocket: WebSocket?, text: String) {
				super.onMessage(webSocket, text)
				printf("client onMessage")
				printf("client message:$text")
			}

			override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
				super.onMessage(webSocket, bytes)
				printf("client onMessage(webSocket: WebSocket, $bytes: ByteString)")
			}

			override fun onClosing(webSocket: WebSocket?, code: Int, reason: String) {
				printf("client onClosing")
				printf("client code:$code reason:$reason")
			}

			override fun onClosed(webSocket: WebSocket?, code: Int, reason: String) {
				printf("client onClosed")
				printf("client code:$code reason:$reason")
			}

			override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
				super.onFailure(webSocket, t, response)
				//出现异常会进入此回调
				printf("client onFailure")
				printf("client throwable:$t")
				printf("client response:$response")
			}
		})


	}
}

















