package top.thsunkist.appkit.engine

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import top.thsunkist.appkit.R
import top.thsunkist.appkit.utility.Debug
import top.thsunkist.appkit.utility.Dimens

@Parcelize
data class ImageUIModel(
    var imageUrl: String? = null,
    var imageRes: Int? = null,
    var placeholder: Int? = null,
    var targetWidth: Int = 0,
    var targetHeight: Int = 0,
) : Parcelable {

    companion object {

        private val displayImageLength = Dimens.dpToPx(50)

        fun fake(): ImageUIModel {
            return ImageUIModel(
                imageUrl = Debug.images.random(),
                placeholder = R.drawable.ic_baseline_image_24
            )
        }

        fun fakeList(): List<ImageUIModel> {
            return (0..(5..10).random()).map {
                ImageUIModel(
                    imageUrl = Debug.images.random(),
                    placeholder = R.drawable.ic_baseline_image_24
                )
            }
        }

        /**
         * 纯粹的Placeholder.
         */
        fun placeholder(imageRes: Int): ImageUIModel {
            return ImageUIModel(
                imageRes = imageRes
            )
        }

        /**
         * 用户/偶像头像.
         */
        fun displayImage(imageUrl: String?): ImageUIModel {
            return ImageUIModel(
                imageUrl = imageUrl,
                placeholder = R.drawable.ic_sharp_account_box_24,
                targetWidth = displayImageLength,
                targetHeight = displayImageLength
            )
        }


        /**
         * 原图.
         */
        fun full(imageUrl: String?): ImageUIModel {
            return ImageUIModel(
                imageUrl = imageUrl,
                placeholder = R.drawable.placeholder_image
            )
        }
    }
}