package top.thsunkist.library.utilities

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
    val marginSmall = dpToPx(4)
    val marginMedium = dpToPx(8)
    val marginLarge = dpToPx(12)
    val marginXLarge = dpToPx(24)
    val marginXXLarge = dpToPx(36)
    val marginXXXLarge = dpToPx(72)

    val fontSizeXXSmall = 8f
    val fontSizeXSmall = 10f
    val fontSizeSmall = 12f
    val fontSizeMedium = 14f
    val fontSizeLarge = 16f
    val fontSizeXLarge = 24f

    val purchaseableItemWidth = dpToPx(100)

    /* Minimum height for purchasable cell is 114dp */
    val purchaseableItemHeight = dpToPx(120)
    /* Minimum height for dance cell is around 65dp */
    val danceItemHeight = dpToPx(75)

    val chatImageMessageItemMaxWidth = dpToPx(200)

    fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }

    val screenWidth: Int by lazy { Resources.getSystem().displayMetrics.widthPixels }
    val screenHeight: Int by lazy { Resources.getSystem().displayMetrics.heightPixels }

    val displayPictureMaxLength: Int = 512
}