package me.taosunkist.hello.data.net.model

import com.mooveit.library.Fakeit
import top.thsunkist.appkit.utility.Debug

data class MultiListItem(
    val uid: Long,
    val avatarUrl: String,
    val name: String,
) {
    companion object {

        fun fakeList(): List<MultiListItem> {
            return (1..20).map { fake() }
        }

        fun fake(): MultiListItem {
            return MultiListItem(
                uid = System.nanoTime(),
                avatarUrl = Debug.images.subList(0, 3).random(),
                name = arrayOf("A", "B", "C").random(),
            )
        }
    }
}