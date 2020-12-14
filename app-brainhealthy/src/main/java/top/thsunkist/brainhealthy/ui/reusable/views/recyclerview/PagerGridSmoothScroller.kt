package top.thsunkist.brainhealthy.ui.reusable.views.recyclerview

import android.util.DisplayMetrics
import android.view.View
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView

class PagerGridSmoothScroller(private val mRecyclerView: RecyclerView) : LinearSmoothScroller(mRecyclerView.context) {

    override fun onTargetFound(targetView: View, state: RecyclerView.State, action: Action) {
        val manager = mRecyclerView.layoutManager ?: return
        if (manager is PagerGridLayoutManager) {
            val pos = mRecyclerView.getChildAdapterPosition(targetView)
            val snapDistances = manager.getSnapOffset(pos)
            val dx = snapDistances[0]
            val dy = snapDistances[1]
            val time = calculateTimeForScrolling(Math.max(Math.abs(dx), Math.abs(dy)))
            if (time > 0) {
                action.update(dx, dy, time, mDecelerateInterpolator)
            }
        }
    }

    override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
        return PagerConfig.millisecondsPreInch / displayMetrics.densityDpi
    }
}
