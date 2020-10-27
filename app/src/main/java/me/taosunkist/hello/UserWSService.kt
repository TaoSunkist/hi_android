package me.taosunkist.hello

import com.google.common.eventbus.Subscribe
import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send
import io.reactivex.Flowable

interface UserWSService {

	@Send
	fun sendBytes(byteArray: ByteArray): Boolean
}
