package me.taosunkist.hello.ui.list.multilist

import me.taosunkist.hello.data.net.model.MultiListItem
import top.thsunkist.tatame.model.ui.ImageUIModel

data class ConversationCellUIModel(

    val nickname: String,


    val imageUIModel: ImageUIModel,
) {

    companion object {

        fun init(conversationItem: MultiListItem): ConversationCellUIModel {

            return ConversationCellUIModel(
                nickname = conversationItem.name,
                imageUIModel = ImageUIModel.displayImage(conversationItem.avatarUrl),
            )
        }
    }
}