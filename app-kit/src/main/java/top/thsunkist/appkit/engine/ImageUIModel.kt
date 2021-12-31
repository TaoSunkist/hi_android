package top.thsunkist.appkit.engine

import android.os.Parcelable
import android.widget.ImageView
import kotlinx.android.parcel.Parcelize
import top.thsunkist.appkit.R
import top.thsunkist.appkit.utility.Debug
import top.thsunkist.appkit.utility.Dimens

/**
 * 对imageview使用到的一些数据进行封装
 */
@Parcelize
data class ImageUIModel constructor(
    var imageUrl: String? = null,
    var imageRes: Int? = null,
    var placeholder: Int? = null,
    var targetWidth: Int = 0,
    var targetHeight: Int = 0,
    var scaleType: ImageView.ScaleType? = ImageView.ScaleType.FIT_CENTER,
) : Parcelable {
    constructor(
        imageUrl: String? = null,
        imageRes: Int? = null,
        placeholder: Int? = null,
        targetWidth: Int = 0,
        targetHeight: Int = 0,
        scaleType: ImageView.ScaleType? = ImageView.ScaleType.FIT_CENTER,
        resizeMode: ResizeMode? = null,
    ) : this(
        imageUrl = resizeMode?.getUrl(imageUrl) ?: imageUrl,
        imageRes = imageRes,
        placeholder = placeholder,
        targetWidth = targetWidth,
        targetHeight = targetHeight,
        scaleType = scaleType
    )

    companion object {

        private val displayImageLength = Dimens.dpToPx(40)
        private val feedThumbnailMaximumWidth = Dimens.screenWidth / 2
        private val chatThumbnailMaximumWidth = (Dimens.screenWidth * 0.75).toInt()

        fun fake(): ImageUIModel {
            return ImageUIModel(
                imageUrl = Debug.images.random(),
                placeholder = R.drawable.default_head
            )
        }

        @JvmStatic
        @SuppressWarnings("unused")
        fun fakeList(): List<ImageUIModel> {
            return (0..(5..10).random()).map {
                ImageUIModel(
                    imageUrl = Debug.images.random(),
                    placeholder = R.drawable.default_head
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
        fun displayImage(imageUrl: String?, resizeMode: ResizeMode? = null): ImageUIModel {
            return ImageUIModel(
                imageUrl = imageUrl,
                placeholder = R.drawable.default_head,
                targetWidth = displayImageLength,
                targetHeight = displayImageLength,
                scaleType = ImageView.ScaleType.CENTER_CROP,
                resizeMode = resizeMode
            )
        }

        /**
         * 聊天的缩略图.
         */
        fun countryTag(imageUrl: String?): ImageUIModel {
            return ImageUIModel(
                imageUrl = imageUrl,
                placeholder = null,
                targetWidth = Dimens.dpToPx(23),
                targetHeight = Dimens.dpToPx(13)
            )
        }

    }
}