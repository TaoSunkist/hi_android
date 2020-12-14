package me.taosunkist.hello.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.forEach
import com.google.protobuf.GeneratedMessageV3
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import me.taosunkist.hello.ProtobufferUtility
import me.taosunkist.hello.databinding.FragmentGorillaWebSocketBinding
import me.taosunkist.hello.hilo.UserProxy
import me.taosunkist.hello.utility.printf
import okhttp3.*
import okio.ByteString
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.concurrent.TimeUnit
import java.util.zip.CRC32


class GorillaWebSocketFragment : BaseFragment() {

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

	private val okHttpClient = OkHttpClient().newBuilder().build()

	lateinit var request: Request

	var webSocket: WebSocket? = null

	private lateinit var binding: FragmentGorillaWebSocketBinding

	private val webSocketListener = object : WebSocketListener() {

		override fun onOpen(webSocket: WebSocket, response: Response) {

			this@GorillaWebSocketFragment.webSocket = webSocket
			requireActivity().runOnUiThread {
				binding.connectWsHostImageView.isSelected = true
				binding.featureButtonsStackLayout.forEach { it.isEnabled = true }
				binding.sendLoginMessageButton.visibility = View.VISIBLE

				binding.sendLoginMessageButton.setOnClickListener { sendLoginMessageButtonPressed() }
				binding.sendHeartMessageButton.setOnClickListener { sendHeartMessageButtonPressed() }

				binding.loggerRecyclerView.logI("client onOpen")
				binding.loggerRecyclerView.logI("client request header:" + response.request().headers())
				binding.loggerRecyclerView.logI("client response header:" + response.headers())
				binding.loggerRecyclerView.logI("client response:$response")
				binding.loggerRecyclerView.logI("client ${this@GorillaWebSocketFragment.webSocket}")
			}
			Observable.interval(4500, TimeUnit.MILLISECONDS).observeOn(Schedulers.io()).subscribe {
				webSocket.send(packet(UserProxy.HeartBeat.newBuilder().setToken(System.currentTimeMillis().toString()).build()))
			}.addTo(compositeDisposable = CompositeDisposable())
		}

		override fun onMessage(webSocket: WebSocket?, text: String) {
			super.onMessage(webSocket, text)

			requireActivity().runOnUiThread {
				binding.loggerRecyclerView.logI("recevied message: ${com.google.protobuf.ByteString.copyFromUtf8(text).toByteArray()}")
			}
		}


		override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
			super.onMessage(webSocket, bytes)

			requireActivity().runOnUiThread {
				unpacket(bytes)
			}
		}

		override fun onClosing(webSocket: WebSocket?, code: Int, reason: String) {
			printf("client code:$code reason:$reason")
			requireActivity().runOnUiThread {
				binding.connectWsHostImageView.isSelected = false
				binding.featureButtonsStackLayout.forEach { it.isEnabled = false }
			}
		}

		override fun onClosed(webSocket: WebSocket?, code: Int, reason: String) {
			printf("client onClosed($webSocket: WebSocket?, $code: Int, $reason: String)")
			requireActivity().runOnUiThread {
				binding.connectWsHostImageView.isSelected = false
				binding.featureButtonsStackLayout.forEach { it.isEnabled = false }
			}
		}

		override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
			super.onFailure(webSocket, t, response)
			requireActivity().runOnUiThread {
				binding.connectWsHostImageView.isSelected = false
				binding.featureButtonsStackLayout.forEach { it.isEnabled = false }
			}
			printf("client onFailure($webSocket: WebSocket, $t: Throwable, $response: Response?)")

		}
	}


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
		binding.loggerRecyclerView.logI("Default java endian: " + ByteOrder.nativeOrder().toString())
		request = Request.Builder().get().url(binding.wesocketUrlEditText.text!!.toString()).build()
	}

	private val crC32 = CRC32()

	private fun unpacket(bytes: ByteString) {
		val remotePacket = bytes.toByteArray()
		printf("client recevied message: ${remotePacket.toList()},\nmessage-size: ${remotePacket.size}")
		val dataPacket = remotePacket.sliceArray(IntRange(0, remotePacket.size - 5))
		/* take checksum via remotePacket */
		val checkSum = ProtobufferUtility.bytes2IntBig(remotePacket.sliceArray(IntRange(remotePacket.size - 4, remotePacket.size - 1)))

		printf("client ${dataPacket.size}, ${remotePacket.size}")
		crC32.apply { reset() }.update(dataPacket)
		val clientCheckSum = crC32.value
		printf("client to verify checkSum $checkSum-$clientCheckSum")

		val byteBuffer = ByteBuffer.wrap(dataPacket)

		val version = byteBuffer.short
		printf("client version: $version", ", capacity: ${byteBuffer.capacity()}, position: ${byteBuffer.position()}")
		val msgType = byteBuffer.int
		printf("client msgType: $msgType", ", capacity: ${byteBuffer.capacity()}, position: ${byteBuffer.position()}")
		val msgID = byteBuffer.long
		printf("client msgID: $msgID", ", capacity: ${byteBuffer.capacity()}, position: ${byteBuffer.position()}")
		val timestamp = byteBuffer.long
		printf("client timestamp: $timestamp", ", capacity: ${byteBuffer.capacity()}, position: ${byteBuffer.position()}")
		val dataLen = byteBuffer.int
		printf("client dataLen: $dataLen", ", capacity: ${byteBuffer.capacity()}, position: ${byteBuffer.position()}")
		val data = ByteArray(dataLen)
		byteBuffer.get(data)

		if (msgType == 2) {
			val loginRsp = UserProxy.LoginRsp.parseFrom(data)
			printf("client dataLen: $loginRsp")
		} else if (msgType == 4) {
			val heartBeatRsp = UserProxy.HeartBeatRsp.parseFrom(data)
			printf("client status: $heartBeatRsp")
		} else if (msgType == 100) {
			val matchSuccess = UserProxy.MatchSuccess.parseFrom(data)
			printf("client status: $matchSuccess")
		}
	}

	private fun packet(generatedMessageV3: GeneratedMessageV3): ByteString? {

		/* 2 uint16 */
		val version: Short = 10

		/* 4 u32 */
		val msgType: Int = 1

		/* 8 u64 */
		val msgID: Long = 1

		/* 8 u64 */
		val timestamp: Long = 10000

		/* 4 u32 */
		val dataLen: Int = generatedMessageV3.toByteArray().size

		val dataBytes = generatedMessageV3.toByteArray()

		val badi = ByteArrayOutputStream()

		badi.write(ProtobufferUtility.shortToByteBig(version))
		badi.write(ProtobufferUtility.intToByteBig(msgType))
		badi.write(ProtobufferUtility.longToBytesBig(msgID))
		badi.write(ProtobufferUtility.longToBytesBig(timestamp))
		badi.write(ProtobufferUtility.intToByteBig(dataLen))
		badi.write(dataBytes)

		val neededCrc32Sum = badi.toByteArray()

		crC32.apply { reset() }.update(neededCrc32Sum)
		val checkSum = crC32.value

		printf("client crc32: $checkSum")
		badi.write(ProtobufferUtility.intToByteBig(checkSum.toInt()))
		return ByteString.of(*badi.toByteArray())
	}

	private fun sendHeartMessageButtonPressed() {
		val heartBeat = UserProxy.HeartBeat.newBuilder().setToken(System.currentTimeMillis().toString()).build()
		val packetResult = packet(generatedMessageV3 = heartBeat)
		printf("client sendHeartMessageButtonPressed result ${webSocket!!.send(packetResult)}")
	}

	private fun sendLoginMessageButtonPressed() {
		val login = UserProxy.Login.newBuilder().setToken(System.currentTimeMillis().toString().repeat(100)).build()
		val packetResult = packet(generatedMessageV3 = login)
		printf("client sendLoginMessageButtonPressed result ${webSocket!!.send(packetResult)}")
	}

	private fun connectWsHostImageViewPressed() {
		if (binding.connectWsHostImageView.isSelected.not()) {
			okHttpClient.newWebSocket(request, webSocketListener)
		} else {
			binding.connectWsHostImageView.isSelected = false
			webSocket?.cancel()
		}
	}
}

















