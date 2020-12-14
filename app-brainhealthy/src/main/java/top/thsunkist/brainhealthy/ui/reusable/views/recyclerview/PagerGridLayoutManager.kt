package top.thsunkist.brainhealthy.ui.reusable.views.recyclerview

import android.annotation.SuppressLint
import android.graphics.PointF
import android.graphics.Rect
import android.util.Log
import android.util.SparseArray
import android.view.View
import android.view.View.MeasureSpec.EXACTLY
import android.view.ViewGroup
import androidx.annotation.IntDef
import androidx.annotation.IntRange
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import kotlin.math.abs

class PagerGridLayoutManager(
    @param:IntRange(from = 1, to = 100) private val mRows: Int,
    @param:IntRange(from = 1, to = 100) private val mColumns: Int,
    @param:OrientationType @field:OrientationType private var mOrientation: Int
) : RecyclerView.LayoutManager(), RecyclerView.SmoothScroller.ScrollVectorProvider {

    private var offsetX = 0

    private val mOnePageSize: Int = mRows * mColumns
    private val mItemFrames: SparseArray<Rect> = SparseArray()
    private var mItemWidth = 0
    private var mItemHeight = 0
    private var mWidthUsed = 0
    private var mHeightUsed = 0
    private var mMaxScrollX: Int = 0
    private var mMaxScrollY: Int = 0
    private var mScrollState = SCROLL_STATE_IDLE
    private var isAllowContinuousScroll = true
    private var mRecyclerView: RecyclerView? = null
    private var mChangeSelectInScrolling = true
    private var mLastPageCount = -1
    private var mLastPageIndex = -1
    private var mPageListener: PageListener? = null

    @IntDef(VERTICAL, HORIZONTAL)
    annotation class OrientationType

    private val usableWidth: Int
        get() = width - paddingLeft - paddingRight
    var offsetY = 0
        private set
    private val usableHeight: Int
        get() = height - paddingTop - paddingBottom
    private val totalPageCount: Int
        get() {
            if (itemCount <= 0) return 0
            var totalCount = itemCount / mOnePageSize
            if (itemCount % mOnePageSize != 0) {
                totalCount++
            }
            return totalCount
        }
    private val pageIndexByOffset: Int
        get() {
            var pageIndex: Int
            if (canScrollVertically()) {
                val pageHeight = usableHeight
                if (offsetY <= 0 || pageHeight <= 0) {
                    pageIndex = 0
                } else {
                    pageIndex = offsetY / pageHeight
                    if (offsetY % pageHeight > pageHeight / 2) {
                        pageIndex++
                    }
                }
            } else {
                val pageWidth = usableWidth
                if (offsetX <= 0 || pageWidth <= 0) {
                    pageIndex = 0
                } else {
                    pageIndex = offsetX / pageWidth
                    if (offsetX % pageWidth > pageWidth / 2) {
                        pageIndex++
                    }
                }
            }
            return pageIndex
        }


    override fun onAttachedToWindow(view: RecyclerView?) {
        super.onAttachedToWindow(view)
        mRecyclerView = view
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State) {
        if (state.isPreLayout || !state.didStructureChange()) {
            return
        }

        if (itemCount == 0) {
            removeAndRecycleAllViews(recycler!!)
            setPageCount(0)
            setPageIndex(0, false)
            return
        } else {
            setPageCount(totalPageCount)
            setPageIndex(pageIndexByOffset, false)
        }

        var mPageCount = itemCount / mOnePageSize
        if (itemCount % mOnePageSize != 0) {
            mPageCount++
        }

        if (canScrollHorizontally()) {
            mMaxScrollX = (mPageCount - 1) * usableWidth
            mMaxScrollY = 0
            if (offsetX > mMaxScrollX) {
                offsetX = mMaxScrollX
            }
        } else {
            mMaxScrollX = 0
            mMaxScrollY = (mPageCount - 1) * usableHeight
            if (offsetY > mMaxScrollY) {
                offsetY = mMaxScrollY
            }
        }

        if (mItemWidth <= 0) {
            mItemWidth = usableWidth / mColumns
        }
        if (mItemHeight <= 0) {
            mItemHeight = usableHeight / mRows
        }

        mWidthUsed = usableWidth - mItemWidth
        mHeightUsed = usableHeight - mItemHeight

        for (i in 0 until mOnePageSize * 2) {
            getItemFrameByPosition(i)
        }

        if (offsetX == 0 && offsetY == 0) {
            for (i in 0 until mOnePageSize) {
                if (i >= itemCount) break
                val view = recycler!!.getViewForPosition(i)
                addView(view)
                measureChildWithMargins(view, mWidthUsed, mHeightUsed)
            }
        }

        recycleAndFillItems(recycler, state, true)
    }

    override fun onLayoutCompleted(state: RecyclerView.State?) {
        super.onLayoutCompleted(state)
        if (state!!.isPreLayout) return
        setPageCount(totalPageCount)
        setPageIndex(pageIndexByOffset, false)
    }

    @SuppressLint("CheckResult")
    private fun recycleAndFillItems(
        recycler: RecyclerView.Recycler?, state: RecyclerView.State,
        isStart: Boolean
    ) {
        if (state.isPreLayout) {
            return
        }

        val displayRect = Rect(
            offsetX - mItemWidth, offsetY - mItemHeight,
            usableWidth + offsetX + mItemWidth, usableHeight + offsetY + mItemHeight
        )
        displayRect.intersect(0, 0, mMaxScrollX + usableWidth, mMaxScrollY + usableHeight)

        var startPos: Int
        val pageIndex = pageIndexByOffset
        startPos = pageIndex * mOnePageSize
        startPos -= mOnePageSize * 2
        if (startPos < 0) {
            startPos = 0
        }
        var stopPos = startPos + mOnePageSize * 4
        if (stopPos > itemCount) {
            stopPos = itemCount
        }

        detachAndScrapAttachedViews(recycler!!)

        if (isStart) {
            for (i in startPos until stopPos) {
                addOrRemove(recycler, displayRect, i)
            }
        } else {
            for (i in stopPos - 1 downTo startPos) {
                addOrRemove(recycler, displayRect, i)
            }
        }
    }

    private fun addOrRemove(recycler: RecyclerView.Recycler, displayRect: Rect, i: Int) {
        val child = recycler.getViewForPosition(i)
        val rect = getItemFrameByPosition(i)
        if (!Rect.intersects(displayRect, rect)) {
            removeAndRecycleView(child, recycler)   // 回收入暂存区
        } else {
            addView(child)
            measureChildWithMargins(child, mWidthUsed, mHeightUsed)
            val lp = child.layoutParams as RecyclerView.LayoutParams
            layoutDecorated(
                child,
                rect.left - offsetX + lp.leftMargin + paddingLeft,
                rect.top - offsetY + lp.topMargin + paddingTop,
                rect.right - offsetX - lp.rightMargin + paddingLeft,
                rect.bottom - offsetY - lp.bottomMargin + paddingTop
            )
        }
    }

    override fun scrollHorizontallyBy(dx: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
        val newX = offsetX + dx
        var result = dx
        if (newX > mMaxScrollX) {
            result = mMaxScrollX - offsetX
        } else if (newX < 0) {
            result = 0 - offsetX
        }
        offsetX += result
        setPageIndex(pageIndexByOffset, true)
        offsetChildrenHorizontal(-result)
        if (result > 0) {
            recycleAndFillItems(recycler, state!!, true)
        } else {
            recycleAndFillItems(recycler, state!!, false)
        }
        return result
    }

    override fun scrollVerticallyBy(dy: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
        val newY = offsetY + dy
        var result = dy
        if (newY > mMaxScrollY) {
            result = mMaxScrollY - offsetY
        } else if (newY < 0) {
            result = 0 - offsetY
        }
        offsetY += result
        setPageIndex(pageIndexByOffset, true)
        offsetChildrenVertical(-result)
        if (result > 0) {
            recycleAndFillItems(recycler, state!!, true)
        } else {
            recycleAndFillItems(recycler, state!!, false)
        }
        return result
    }

    override fun onScrollStateChanged(state: Int) {
        mScrollState = state
        super.onScrollStateChanged(state)
        if (state == SCROLL_STATE_IDLE) {
            setPageIndex(pageIndexByOffset, false)
        }
    }

    private fun getItemFrameByPosition(pos: Int): Rect {
        var rect: Rect? = mItemFrames.get(pos)
        if (null == rect) {
            rect = Rect()
            val page = pos / mOnePageSize
            var offsetX = 0
            var offsetY = 0
            if (canScrollHorizontally()) {
                offsetX += usableWidth * page
            } else {
                offsetY += usableHeight * page
            }
            val pagePos = pos % mOnePageSize
            val row = pagePos / mColumns
            val col = pagePos - row * mColumns

            offsetX += col * mItemWidth
            offsetY += row * mItemHeight
            rect.left = offsetX
            rect.top = offsetY
            rect.right = offsetX + mItemWidth
            rect.bottom = offsetY + mItemHeight

            mItemFrames.put(pos, rect)
        }
        return rect
    }

    private fun getPageIndexByPos(pos: Int): Int {
        return pos / mOnePageSize
    }

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onMeasure(
        recycler: RecyclerView.Recycler,
        state: RecyclerView.State,
        widthMeasureSpec: Int,
        heightMeasureSpec: Int
    ) {
        super.onMeasure(recycler, state, widthMeasureSpec, heightMeasureSpec)
        val widthsize = View.MeasureSpec.getSize(widthMeasureSpec)
        var widthmode = View.MeasureSpec.getMode(widthMeasureSpec)

        val heightsize = View.MeasureSpec.getSize(heightMeasureSpec)
        var heightmode = View.MeasureSpec.getMode(heightMeasureSpec)

        if (widthmode != EXACTLY && widthsize > 0) {
            widthmode = EXACTLY
        }
        if (heightmode != EXACTLY && heightsize > 0) {
            heightmode = EXACTLY
        }
        setMeasuredDimension(
            View.MeasureSpec.makeMeasureSpec(widthsize, widthmode),
            View.MeasureSpec.makeMeasureSpec(heightsize, heightmode)
        )
    }


    override fun canScrollHorizontally(): Boolean {
        return mOrientation == HORIZONTAL
    }

    override fun canScrollVertically(): Boolean {
        return mOrientation == VERTICAL
    }

    internal fun findNextPageFirstPos(): Int {
        var page = mLastPageIndex
        page++
        if (page >= totalPageCount) {
            page = totalPageCount - 1
        }
        return page * mOnePageSize
    }

    internal fun findPrePageFirstPos(): Int {
        // 在获取时由于前一页的View预加载出来了，所以获取到的直接就是前一页
        var page = mLastPageIndex
        page--
        if (page < 0) {
            page = 0
        }
        return page * mOnePageSize
    }

    override fun computeScrollVectorForPosition(targetPosition: Int): PointF? {
        val vector = PointF()
        val pos = getSnapOffset(targetPosition)
        vector.x = pos[0].toFloat()
        vector.y = pos[1].toFloat()
        return vector
    }

    internal fun getSnapOffset(targetPosition: Int): IntArray {
        val offset = IntArray(2)
        val pos = getPageLeftTopByPosition(targetPosition)
        offset[0] = pos[0] - offsetX
        offset[1] = pos[1] - offsetY
        return offset
    }

    private fun getPageLeftTopByPosition(pos: Int): IntArray {
        val leftTop = IntArray(2)
        val page = getPageIndexByPos(pos)
        if (canScrollHorizontally()) {
            leftTop[0] = page * usableWidth
            leftTop[1] = 0
        } else {
            leftTop[0] = 0
            leftTop[1] = page * usableHeight
        }
        return leftTop
    }

    fun findSnapView(): View? {
        if (null != focusedChild) {
            return focusedChild
        }
        if (childCount <= 0) {
            return null
        }
        val targetPos = pageIndexByOffset * mOnePageSize   // 目标Pos
        for (i in 0 until childCount) {
            val childPos = getPosition(getChildAt(i)!!)
            if (childPos == targetPos) {
                return getChildAt(i)
            }
        }
        return getChildAt(0)
    }

    private fun setPageCount(pageCount: Int) {
        if (pageCount >= 0) {
            if (mPageListener != null && pageCount != mLastPageCount) {
                mPageListener!!.onPageSizeChanged(pageCount)
            }
            mLastPageCount = pageCount
        }
    }

    private fun setPageIndex(pageIndex: Int, isScrolling: Boolean) {
        if (pageIndex == mLastPageIndex) return
        if (isAllowContinuousScroll) {
            mLastPageIndex = pageIndex
        } else {
            if (!isScrolling) {
                mLastPageIndex = pageIndex
            }
        }
        if (isScrolling && !mChangeSelectInScrolling) return
        if (pageIndex >= 0) {
            if (null != mPageListener) {
                mPageListener!!.onPageSelect(pageIndex)
            }
        }
    }

    fun setChangeSelectInScrolling(changeSelectInScrolling: Boolean) {
        mChangeSelectInScrolling = changeSelectInScrolling
    }

    @OrientationType
    fun setOrientationType(@OrientationType orientation: Int): Int {
        if (mOrientation == orientation || mScrollState != SCROLL_STATE_IDLE) return mOrientation
        mOrientation = orientation
        mItemFrames.clear()
        val x = offsetX
        val y = offsetY
        offsetX = y / usableHeight * usableWidth
        offsetY = x / usableWidth * usableHeight
        val mx = mMaxScrollX
        val my = mMaxScrollY
        mMaxScrollX = my / usableHeight * usableWidth
        mMaxScrollY = mx / usableWidth * usableHeight
        return mOrientation
    }

    override fun smoothScrollToPosition(recyclerView: RecyclerView?, state: RecyclerView.State?, position: Int) {
        val targetPageIndex = getPageIndexByPos(position)
        smoothScrollToPage(targetPageIndex)
    }

    fun smoothPrePage() {
        smoothScrollToPage(pageIndexByOffset - 1)
    }

    fun smoothNextPage() {
        smoothScrollToPage(pageIndexByOffset + 1)
    }

    fun smoothScrollToPage(pageIndex: Int) {
        if (pageIndex < 0 || pageIndex >= mLastPageCount) {
            Log.e(TAG, "pageIndex is outOfIndex, must in [0, $mLastPageCount).")
            return
        }
        if (null == mRecyclerView) {
            Log.e(TAG, "RecyclerView Not Found!")
            return
        }

        val currentPageIndex = pageIndexByOffset
        if (abs(pageIndex - currentPageIndex) > 3) {
            if (pageIndex > currentPageIndex) {
                scrollToPage(pageIndex - 3)
            } else if (pageIndex < currentPageIndex) {
                scrollToPage(pageIndex + 3)
            }
        }
        val smoothScroller = PagerGridSmoothScroller(mRecyclerView!!)
        val position = pageIndex * mOnePageSize
        smoothScroller.targetPosition = position
        startSmoothScroll(smoothScroller)
    }

    override fun scrollToPosition(position: Int) {
        val pageIndex = getPageIndexByPos(position)
        scrollToPage(pageIndex)
    }

    fun prePage() {
        scrollToPage(pageIndexByOffset - 1)
    }

    fun nextPage() {
        scrollToPage(pageIndexByOffset + 1)
    }

    private fun scrollToPage(pageIndex: Int) {
        if (pageIndex < 0 || pageIndex >= mLastPageCount) {
            Log.e(TAG, "pageIndex = $pageIndex is out of bounds, mast in [0, $mLastPageCount)")
            return
        }

        if (null == mRecyclerView) {
            Log.e(TAG, "RecyclerView Not Found!")
            return
        }

        var mTargetOffsetXBy: Int
        var mTargetOffsetYBy: Int
        if (canScrollVertically()) {
            mTargetOffsetXBy = 0
            mTargetOffsetYBy = pageIndex * usableHeight - offsetY
        } else {
            mTargetOffsetXBy = pageIndex * usableWidth - offsetX
            mTargetOffsetYBy = 0
        }
        mRecyclerView!!.scrollBy(mTargetOffsetXBy, mTargetOffsetYBy)
        setPageIndex(pageIndex, false)
    }

    fun setPageListener(pageListener: PageListener) {
        mPageListener = pageListener
    }

    interface PageListener {
        fun onPageSizeChanged(pageSize: Int)

        fun onPageSelect(pageIndex: Int)
    }

    companion object {
        private val TAG = PagerGridLayoutManager::class.java.simpleName

        const val VERTICAL = 0
        const val HORIZONTAL = 1
    }
}