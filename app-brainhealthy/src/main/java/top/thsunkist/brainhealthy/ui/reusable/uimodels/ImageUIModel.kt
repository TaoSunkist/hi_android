package top.thsunkist.brainhealthy.ui.reusable.uimodels

import android.os.Parcelable
import android.widget.ImageView
import top.thsunkist.brainhealthy.utilities.Debug
import top.thsunkist.brainhealthy.utilities.view.Dimens
import top.thsunkist.brainhealthy.R
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ImageUIModel(
    var imageUrl: String? = null,
    var imageRes: Int? = null,
    var placeholder: Int? = null,
    var targetWidth: Int = 0,
    var targetHeight: Int = 0,
    var scaleType: ImageView.ScaleType? = ImageView.ScaleType.FIT_CENTER
) : Parcelable {

    companion object {

        private val displayImageLength = Dimens.dpToPx(50)
        private val feedThumbnailMaximumWidth = Dimens.screenWidth / 2
        private val chatThumbnailMaximumWidth = (Dimens.screenWidth * 0.75).toInt()

        fun fake(): ImageUIModel {
            return ImageUIModel(
                imageUrl = Debug.images.random(),
                placeholder = R.drawable.bg_placeholder_image
            )
        }

        fun fakeList(): List<ImageUIModel> {
            return (0..(5..10).random()).map {
                ImageUIModel(
                    imageUrl = Debug.images.random(),
                    placeholder = R.drawable.bg_placeholder_image
                )
            }
        }

        /**
         * 纯粹的Placeholder.
         */
        fun placeholder(imageRes: Int, scaleType: ImageView.ScaleType = ImageView.ScaleType.FIT_CENTER): ImageUIModel {
            return ImageUIModel(
                imageRes = imageRes,
                scaleType = scaleType
            )
        }

        /**
         * 用户/偶像头像.
         */
        fun displayImage(imageUrl: String?): ImageUIModel {
            return ImageUIModel(
                imageUrl = imageUrl,
                placeholder = R.drawable.rc_default_portrait,
                targetWidth = displayImageLength,
                targetHeight = displayImageLength,
                scaleType = ImageView.ScaleType.CENTER_CROP
            )
        }

        /**
         * 首页的缩略图.
         */
        fun onlineThumbnail(imageUrl: String?): ImageUIModel {
            return ImageUIModel(
                imageUrl = imageUrl,
                placeholder = R.drawable.bg_placeholder_image,
                targetWidth = feedThumbnailMaximumWidth
            )
        }

        /**
         * 聊天的缩略图.
         */
        fun chatThumbnail(imageUrl: String?): ImageUIModel {
            return ImageUIModel(
                imageUrl = imageUrl,
                placeholder = R.drawable.bg_placeholder_image,
                targetWidth = chatThumbnailMaximumWidth
            )
        }

        /**
         * 聊天的缩略图.
         */
        fun countryTag(imageUrl: String?): ImageUIModel {
            return ImageUIModel(
                imageUrl = imageUrl,
                placeholder = R.drawable.ic_placeholder_image,
                targetWidth = Dimens.dpToPx(23),
                targetHeight = Dimens.dpToPx(13)
            )
        }

        /**
         * 原图.
         */
        fun full(imageUrl: String?): ImageUIModel {
            return ImageUIModel(
                imageUrl = imageUrl,
                placeholder = R.drawable.bg_placeholder_image
            )
        }
    }
}