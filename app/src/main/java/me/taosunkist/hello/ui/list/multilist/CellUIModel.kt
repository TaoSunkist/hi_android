package me.taosunkist.hello.ui.list.multilist

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import me.taosunkist.hello.data.net.model.MultiListItem
import top.thsunkist.appkit.engine.ImageUIModel

data class CellUIModel(
    @Bindable
    val nickname: String,
    val imageUIModel: ImageUIModel,
) : BaseObservable() {

    companion object {

        fun init(conversationItem: MultiListItem): CellUIModel {

            return CellUIModel(
                nickname = conversationItem.name,
                imageUIModel = ImageUIModel.displayImage(conversationItem.avatarUrl),
            )
        }
    }
}