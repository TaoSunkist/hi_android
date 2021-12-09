package me.taosunkist.hello.data.net

import com.tinder.scarlet.ws.Send

interface UserWebsocketor {

	@Send
	fun sendBytes(byteArray: ByteArray)
}
