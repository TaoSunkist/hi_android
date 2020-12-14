package top.thsunkist.brainhealthy.ui.reusable.views.recyclerview

import android.view.View
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper

class PagerGridSnapHelper : SnapHelper() {
    private var mRecyclerView: RecyclerView? = null

    @Throws(IllegalStateException::class)
    override fun attachToRecyclerView(recyclerView: RecyclerView?) {
        super.attachToRecyclerView(recyclerView)
        mRecyclerView = recyclerView
    }

    override fun calculateDistanceToFinalSnap(
        layoutManager: RecyclerView.LayoutManager,
        targetView: View
    ): IntArray? {
        val pos = layoutManager.getPosition(targetView)
        var offset = IntArray(2)
        if (layoutManager is PagerGridLayoutManager) {
            offset = layoutManager.getSnapOffset(pos)
        }
        return offset
    }

    override fun findSnapView(layoutManager: RecyclerView.LayoutManager): View? {
        return if (layoutManager is PagerGridLayoutManager) {
            layoutManager.findSnapView()
        } else null
    }

    override fun findTargetSnapPosition(
        layoutManager: RecyclerView.LayoutManager?,
        velocityX: Int, velocityY: Int
    ): Int {
        var target = RecyclerView.NO_POSITION
        if (null != layoutManager && layoutManager is PagerGridLayoutManager) {
            val manager = layoutManager as PagerGridLayoutManager?
            if (manager!!.canScrollHorizontally()) {
                if (velocityX > PagerConfig.flingThreshold) {
                    target = manager.findNextPageFirstPos()
                } else if (velocityX < -PagerConfig.flingThreshold) {
                    target = manager.findPrePageFirstPos()
                }
            } else if (manager.canScrollVertically()) {
                if (velocityY > PagerConfig.flingThreshold) {
                    target = manager.findNextPageFirstPos()
                } else if (velocityY < -PagerConfig.flingThreshold) {
                    target = manager.findPrePageFirstPos()
                }
            }
        }
        return target
    }

    override fun onFling(velocityX: Int, velocityY: Int): Boolean {
        val layoutManager = mRecyclerView!!.layoutManager ?: return false
        val adapter = mRecyclerView!!.adapter ?: return false
        val minFlingVelocity = PagerConfig.flingThreshold
        return (Math.abs(velocityY) > minFlingVelocity || Math.abs(velocityX) > minFlingVelocity) && snapFromFling(
            layoutManager,
            velocityX,
            velocityY
        )
    }

    private fun snapFromFling(
        layoutManager: RecyclerView.LayoutManager, velocityX: Int,
        velocityY: Int
    ): Boolean {
        if (layoutManager !is RecyclerView.SmoothScroller.ScrollVectorProvider) {
            return false
        }

        val smoothScroller = createSnapScroller(layoutManager) ?: return false

        val targetPosition = findTargetSnapPosition(layoutManager, velocityX, velocityY)
        if (targetPosition == RecyclerView.NO_POSITION) {
            return false
        }

        smoothScroller.targetPosition = targetPosition
        layoutManager.startSmoothScroll(smoothScroller)
        return true
    }

    override fun createSnapScroller(layoutManager: RecyclerView.LayoutManager?): LinearSmoothScroller? {
        return if (layoutManager !is RecyclerView.SmoothScroller.ScrollVectorProvider) {
            null
        } else PagerGridSmoothScroller(mRecyclerView!!)
    }

    fun setFlingThreshold(threshold: Int) {
        PagerConfig.flingThreshold = threshold
    }
}