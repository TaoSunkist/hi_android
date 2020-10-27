package me.taosunkist.hello.ui

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.messageadapter.protobuf.ProtobufMessageAdapter
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import me.taosunkist.hello.ProtobufferUtility
import me.taosunkist.hello.UserWSService
import me.taosunkist.hello.databinding.FragmentGorillaWebSocketBinding
import me.taosunkist.hello.hilo.UserProxy
import me.taosunkist.hello.utility.printf
import okhttp3.*
import okio.ByteString
import org.jetbrains.annotations.Async
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.zip.CRC32


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

	private val heartBeat = UserProxy.Login.newBuilder().setToken(System.currentTimeMillis().toString()).build()

	/* 2 uint16 */
	val version: Short = 10

	/* 4 u32 */
	private val msgType: Int = 1

	/* 8 u64 */
	private val msgID: Long = 1

	/* 8 u64 */
	private val timestamp: Long = 10000

	/* 4 u32 */
	private val dataLen: Int = heartBeat.toByteArray().size
	private val dataBytes = heartBeat.toByteArray()
	private val badi = ByteArrayOutputStream()
	private val okHttpClient = OkHttpClient().newBuilder().build()
	lateinit var request: Request
	var mWebSocket: WebSocket? = null

	init {

		badi.write(ProtobufferUtility.shortToByteBig(version))
		badi.write(ProtobufferUtility.intToByteBig(msgType))
		badi.write(ProtobufferUtility.longToBytesBig(msgID))
		badi.write(ProtobufferUtility.longToBytesBig(timestamp))
		badi.write(ProtobufferUtility.intToByteBig(dataLen))
		badi.write(dataBytes)

		val neededCrc32Sum = badi.toByteArray()
		val crC32 = CRC32()
		crC32.update(neededCrc32Sum)
		val checkSum = crC32.value

		printf("client crc32: $checkSum")
		badi.write(ProtobufferUtility.intToByteBig(checkSum.toInt()))
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		binding.connectWsHostImageView.setOnClickListener { connectWsHostImageViewPressed() }

		binding.loggerRecyclerView.logI("Default java endian: " + ByteOrder.nativeOrder().toString())
		binding.loggerRecyclerView.logI("${ByteString.of(*badi.toByteArray())}")

		request = Request.Builder().get()
			.url(binding.wesocketUrlEditText.text!!.toString())
			.build()
	}

	private val webSocketListener = object : WebSocketListener() {
		override fun onOpen(webSocket: WebSocket, response: Response) {
			mWebSocket = webSocket
			requireActivity().runOnUiThread {
				binding.connectWsHostImageView.isSelected = true
				binding.sendLoginMessageButton.visibility = View.VISIBLE
				binding.sendLoginMessageButton.setOnClickListener { sendLoginMessageButtonPressed() }

				binding.loggerRecyclerView.logI("client onOpen")
				binding.loggerRecyclerView.logI("client request header:" + response.request().headers())
				binding.loggerRecyclerView.logI("client response header:" + response.headers())
				binding.loggerRecyclerView.logI("client response:$response")
				binding.loggerRecyclerView.logI("client $mWebSocket")
			}
		}

		override fun onMessage(webSocket: WebSocket?, text: String) {
			super.onMessage(webSocket, text)
			requireActivity().runOnUiThread {
				binding.loggerRecyclerView.logI("recevied message: ${com.google.protobuf.ByteString.copyFromUtf8(text).toByteArray()}")
			}
		}


		@RequiresApi(Build.VERSION_CODES.O)
		override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
			super.onMessage(webSocket, bytes)
			requireActivity().runOnUiThread {
				val completeDataPacket = bytes.toByteArray()
				printf("client recevied message: ${completeDataPacket.toList()},\nmessage-size: ${completeDataPacket.size}")
				val dataPacket = completeDataPacket.sliceArray(IntRange(0, completeDataPacket.size - 5))
				val checkSum = ProtobufferUtility.bytes2IntBig(completeDataPacket.sliceArray(IntRange(completeDataPacket.size - 4, completeDataPacket.size - 1)))

				printf("client ${dataPacket.size}, ${completeDataPacket.size}")
				val crC32 = CRC32()
				crC32.update(ByteBuffer.wrap(dataPacket))
				val clientCheckSum = crC32.value
				printf("client: $checkSum-$clientCheckSum")

				val byteBuffer = ByteBuffer.wrap(dataPacket)
				printf("client version: $version", ", capacity: ${byteBuffer.capacity()}, position: ${byteBuffer.position()}")
				var version = byteBuffer.short
				printf("client version: $version", ", capacity: ${byteBuffer.capacity()}, position: ${byteBuffer.position()}")
				var msgType = byteBuffer.int
				printf("client msgType: $msgType", ", capacity: ${byteBuffer.capacity()}, position: ${byteBuffer.position()}")
				var msgID = byteBuffer.long
				printf("client msgID: $msgID", ", capacity: ${byteBuffer.capacity()}, position: ${byteBuffer.position()}")
				var timestamp = byteBuffer.long
				printf("client timestamp: $timestamp", ", capacity: ${byteBuffer.capacity()}, position: ${byteBuffer.position()}")
				var dataLen = byteBuffer.int
				printf("client dataLen: $dataLen", ", capacity: ${byteBuffer.capacity()}, position: ${byteBuffer.position()}")
				val data = ByteArray(dataLen)
				byteBuffer.get(data)
				printf("client dataLen: ${UserProxy.LoginRsp.parseFrom(data)}")
			}
		}

		override fun onClosing(webSocket: WebSocket?, code: Int, reason: String) {
			printf("client code:$code reason:$reason")
		}

		override fun onClosed(webSocket: WebSocket?, code: Int, reason: String) {
			printf("client onClosed($webSocket: WebSocket?, $code: Int, $reason: String)")
			requireActivity().runOnUiThread {
				binding.connectWsHostImageView.isSelected = false
			}
		}

		override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
			super.onFailure(webSocket, t, response)
			requireActivity().runOnUiThread {
				binding.connectWsHostImageView.isSelected = false
			}
			printf("client onFailure($webSocket: WebSocket, $t: Throwable, $response: Response?)")

		}
	}

	private fun sendLoginMessageButtonPressed() {
		printf("client send result ${mWebSocket!!.send(ByteString.of(*badi.toByteArray()))}")
	}

	private fun connectWsHostImageViewPressed() {
		if (binding.connectWsHostImageView.isSelected.not()) {
			okHttpClient.newWebSocket(request, webSocketListener)
		} else {
			binding.connectWsHostImageView.isSelected = false
			mWebSocket?.cancel()
		}

		val scarlet = Scarlet.Builder()
			.webSocketFactory(okHttpClient.newWebSocketFactory("ws://192.168.1.180:8080/ws"))
			.addMessageAdapterFactory(ProtobufMessageAdapter.Factory())
			.build()
		printf("${badi.toByteArray().asList()}")

		val userWSService = scarlet.create(UserWSService::class.java)
		userWSService.sendBytes(badi.toByteArray()).observeOn(Schedulers.io()).subscribe {
			printf("client $it")
		}.addTo(compositeDisposable = CompositeDisposable())
	}
}

















