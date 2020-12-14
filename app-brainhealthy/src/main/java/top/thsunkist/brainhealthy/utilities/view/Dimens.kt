package top.thsunkist.brainhealthy.utilities.view

import android.content.res.Resources
import android.graphics.Rect
import com.jakewharton.rxrelay2.BehaviorRelay

object Dimens {

    private var safeAreaCalculated: Boolean = false
    val safeAreaRelay = BehaviorRelay.create<Rect>()
    var safeArea: Rect = Rect()
        set(rect) {
            field = rect
            if (safeAreaCalculated.not()) {
                safeAreaRelay.accept(rect)
                safeAreaCalculated = true
            }
        }

    /* Make sure following constants match dimens.xml */
    val marginXSmall = dpToPx(2)
    val marginSmall = dpToPx(5)
    val marginMedium = dpToPx(8)
    val marginLarge = dpToPx(12)
    val marginXLarge = dpToPx(24)
    val marginXXLarge = dpToPx(36)
    val marginXXXLarge = dpToPx(72)

    const val fontSizeXXSmall = 8f
    val fontSizeXSmall = 10f
    val fontSizeSmall = 12f
    val fontSizeMedium = 14f
    val fontSizeLarge = 16f
    val fontSizeXLarge = 24f

    val purchaseableItemWidth = dpToPx(100)

    /* Minimum height for purchasable cell is 114dp */
    val purchaseableItemHeight = dpToPx(100)

    val smallAvatarSize = 60

    val mediumAvatarSize = 120

    val largeAvatarSize = 240

    val purchaseableDiamondItemHeight = dpToPx(120)

    val circleButtonDhefaultItemHeight = dpToPx(50)


    fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }

    val screenWidth: Int by lazy { Resources.getSystem().displayMetrics.widthPixels }
    val screenHeight: Int by lazy { Resources.getSystem().displayMetrics.heightPixels }

    const val displayPictureMaxLength: Int = 512
}