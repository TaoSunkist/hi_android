package me.taosunkist.hello.ui.list.multilist

import me.taosunkist.hello.data.net.model.MultiListItem
import top.thsunkist.tatame.model.ui.ImageUIModel

data class CelllUIModel(
    val nickname: String,
    val imageUIModel: ImageUIModel,
) {

    companion object {

        fun init(conversationItem: MultiListItem): CelllUIModel {
val nano = System.nanoTime()

            return CelllUIModel(
                nickname = conversationItem.name,
                imageUIModel = ImageUIModel.displayImage(conversationItem.avatarUrl),
            )
        }
    }
}