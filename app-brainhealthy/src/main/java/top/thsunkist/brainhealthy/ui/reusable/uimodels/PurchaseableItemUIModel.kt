package top.thsunkist.brainhealthy.ui.reusable.uimodels

import androidx.annotation.DrawableRes
import com.mooveit.library.Fakeit
import top.thsunkist.brainhealthy.utilities.Debug

data class PurchaseableItemUIModel(
    val itemId: Long,
    var imageUIModel: ImageUIModel,
    val itemName: String?,
    var isSelected: Boolean,
    val tagName: String?,
    @DrawableRes val nameStartDrawableResId: Int? = null,
    @DrawableRes val tagTitleStartDrawableResid: Int? = null,
) {

    companion object {
        var currID: Long = 0
        fun fake(): PurchaseableItemUIModel {

            return PurchaseableItemUIModel(
                itemId = currID++,
                imageUIModel = ImageUIModel(Debug.images.random()),
                isSelected = false,
                itemName = Fakeit.app().name(),
                tagName = "${(1..9999).random()}"
            )
        }


    }
}