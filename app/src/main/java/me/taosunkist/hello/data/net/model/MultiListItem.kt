package me.taosunkist.hello.data.net.model

import com.mooveit.library.Fakeit
import me.taosunkist.hello.utility.Debug
import top.thsunkist.tatame.model.ui.ImageUIModel

data class MultiListItem(
    val avatarUrl: String,
    val name: String,
) {
    companion object {

        fun fakeList(): List<MultiListItem> {
            return (1..20).map { fake() }
        }

        fun fake(): MultiListItem {
            return MultiListItem(
                avatarUrl = Debug.images.random(),
                name = Fakeit.book().author(),
            )
        }
    }
}