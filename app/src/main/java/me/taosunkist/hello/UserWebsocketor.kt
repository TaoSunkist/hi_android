package me.taosunkist.hello

import com.tinder.scarlet.ws.Send

interface UserWebsocketor {

	@Send
	fun sendBytes(byteArray: ByteArray)
}
