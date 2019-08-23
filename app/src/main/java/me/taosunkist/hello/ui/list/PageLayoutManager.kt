package me.taosunkist.hello.ui.list

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.graphics.Rect
import android.util.SparseArray
import android.util.SparseBooleanArray
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.sign

class PageLayoutManager(rows: Int, columns: Int) : RecyclerView.LayoutManager() {
    private val mRecyclerView: RecyclerView? = null
    private var totalWidth: Int = 0
    private var rows = 0
    private var columns = 0
    private var onePageSize = 0

    private var itemWidth = 0
    private var itemHeight = 0

    private var itemWidthUsed: Int = 0
    private var itemHeightUsed: Int = 0

    private var marginsWidth = 0
    private var marginsHeight = 0

    private var offsetX = 0

    private var atEdge = false
    private var startedAnimation = true
    private var edgeDir = 0
    private var dir = 0
    private var marginRect: Rect? = null

    private val allItemFrames = SparseArray<Rect>()
    private val itemStates = SparseBooleanArray()

    private val animatorListener = PageAnimatorListener()
    private var updateListener: ValueAnimator.AnimatorUpdateListener? = null
    private var lastNum: Int = 0
    private var animator: ValueAnimator? = null

    private// 计算RecyclerView的可用高度，除去上下Padding值
    val horizontalSpace: Int
        get() = width - paddingRight - paddingLeft

    private val usableWidth: Int
        get() = width - paddingLeft - paddingRight

    private val usableHeight: Int
        get() = height - paddingTop - paddingBottom

    init {
        if (rows < 1) {
            throw IllegalArgumentException("row count should be at least 1. Provided $rows")
        }
        if (columns < 1) {
            throw IllegalArgumentException("column count should be at least 1. Provided $columns")
        }
        this.rows = rows
        this.columns = columns
        onePageSize = rows * columns
    }

    fun setMarginHorizontal(marginHorizontal: Int) {
        marginsWidth = marginHorizontal
    }

    fun setMarginVertical(marginVertical: Int) {
        marginsHeight = marginVertical
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        if (itemCount <= 0 || state!!.isPreLayout) {
            return
        }
        /* 这个方法主要用于计算并保存每个ItemView的位置 */
        calculateChildrenSite(recycler!!)
        recycleAndFillItems(recycler)
    }

    private fun recycleAndFillItems(recycler: RecyclerView.Recycler) {
        //        long time=System.currentTimeMillis();
        val displayRect = Rect(
                paddingLeft - width / 2 + offsetX,
                paddingTop,
                width * 3 / 2 + offsetX - paddingLeft - paddingRight,
                height - paddingTop - paddingBottom)
        if (childCount == 0) {
            // 重新显示需要出现在屏幕的子View
            val start = if (lastNum - 2 * onePageSize > 0) lastNum - 2 * onePageSize else 0
            val end = if (2 * onePageSize + lastNum > itemCount)
                itemCount
            else
                2 * onePageSize + lastNum
            for (i in start until end) {
                // 判断ItemView的位置和当前显示区域是否重合
                buildRectByPosition(i)
                if (Rect.intersects(displayRect, allItemFrames.get(i))) {
                    // 获得Recycler中缓存的View
                    val itemView = recycler!!.getViewForPosition(i)
                    addView(itemView)
                    measureChildWithMargins(itemView, itemWidthUsed, itemHeightUsed)
                    // 添加View到RecyclerView上
                    // 取出先前存好的ItemView的位置矩形
                    val rect = allItemFrames.get(i)
                    // 将这个item布局出来
                    layoutDecorated(
                            itemView, rect.left - offsetX, rect.top, rect.right - offsetX, rect.bottom)
                    itemStates.put(i, true) // 更新该View的状态为依附
                }
            }
        } else {
            val childRect = Rect()
            val firstPosition = getPosition(getChildAt(0)!!)
            val lastPosition = getPosition(getChildAt(childCount - 1)!!)
            for (i in 0 until childCount) {
                // 这个方法获取的是RecyclerView中的View，注意区别Recycler中的View
                // 这获取的是实际的View
                val child = getChildAt(i)

                // 下面几个方法能够获取每个View占用的空间的位置信息，包括ItemDecorator
                childRect.left = getDecoratedLeft(child!!) + offsetX
                childRect.top = getDecoratedTop(child)
                childRect.right = getDecoratedRight(child) + offsetX
                childRect.bottom = getDecoratedBottom(child)
                // 如果Item没有在显示区域，就说明需要回收
                if (!Rect.intersects(displayRect, childRect)) {
                    // 移除并回收掉滑出屏幕的View
                    removeAndRecycleView(child, recycler)
                    itemStates.put(getPosition(child), false) // 更新该View的状态为未依附
                }
            }
            if (dir > 0) {
                val end = if (onePageSize + lastPosition > itemCount)
                    itemCount
                else
                    onePageSize + lastPosition
                for (i in firstPosition until end) {
                    buildRectByPosition(i)
                    if (Rect.intersects(displayRect, allItemFrames.get(i))) {
                        if (itemStates.get(i)) {
                            continue
                        }
                        addItemView(recycler, i)
                    }
                }
            } else {
                val start = if (firstPosition - onePageSize > 0) firstPosition - onePageSize else 0
                for (i in lastPosition downTo start) {
                    buildRectByPosition(i)
                    if (Rect.intersects(displayRect, allItemFrames.get(i))) {
                        if (itemStates.get(i)) {
                            continue
                        }
                        addItemView(recycler!!, i, 0)
                    }
                }
            }
        }
    }

    private fun addItemView(recycler: RecyclerView.Recycler, viewPosition: Int, toIndex: Int = -1) {
        // 获得Recycler中缓存的View
        val itemView = recycler.getViewForPosition(viewPosition)
        measureChildWithMargins(itemView, itemWidthUsed, itemHeightUsed)
        // 添加View到RecyclerView上
        addView(itemView, toIndex)
        // 取出先前存好的ItemView的位置矩形
        val rect = allItemFrames.get(viewPosition)
        // 将这个item布局出来
        layoutDecorated(itemView, rect.left - offsetX, rect.top, rect.right - offsetX, rect.bottom)
        itemStates.put(viewPosition, true) // 更新该View的状态为依附
    }

    private fun calculateChildrenSite(recycler: RecyclerView.Recycler) {
        //        long time=System.currentTimeMillis();
        val v0 = recycler.getViewForPosition(0)
        val layoutParams = v0.layoutParams as ViewGroup.MarginLayoutParams
        marginRect = Rect()
        marginRect!!.left = layoutParams.leftMargin
        marginRect!!.top = layoutParams.topMargin
        marginRect!!.right = layoutParams.rightMargin
        marginRect!!.bottom = layoutParams.bottomMargin
        // 获取每个Item的平均宽高
        itemWidth = (usableWidth - marginsWidth * 2) / columns - (marginRect!!.left + marginRect!!.right)
        itemHeight = (usableHeight - marginsHeight * 2) / rows - (marginRect!!.top + marginRect!!.bottom)

        // 计算宽高已经使用的量，主要用于后期测量
        itemWidthUsed = (columns - 1) * (itemWidth + (marginRect!!.left + marginRect!!.right)) + marginsWidth * 2
        itemHeightUsed = (rows - 1) * (itemHeight + (marginRect!!.top + marginRect!!.bottom)) + marginsHeight * 2

        // 计算总的页数
        val pageSize = itemCount / onePageSize + if (itemCount % onePageSize == 0) 0 else 1

        // 计算可以横向滚动的最大值
        totalWidth = pageSize * width
        if (getChildAt(0) != null) lastNum = getPosition(getChildAt(0)!!)
        detachAndScrapAttachedViews(recycler)
        itemStates.clear()
        allItemFrames.clear()
        //        System.out.println("calculateChildrenSite:"+(System.currentTimeMillis()-time));
    }

    private fun buildRectByPosition(position: Int) {
        val p = position / onePageSize
        val remainder = position % onePageSize
        val r = remainder / columns
        val c = remainder % columns

        var rect: Rect? = allItemFrames.get(position)
        if (rect == null) {
            rect = Rect()
        }
        val x = p * usableWidth + marginsWidth + c * (itemWidth + marginRect!!.left + marginRect!!.right)
        val y = marginsHeight + r * (itemHeight + marginRect!!.top + marginRect!!.bottom)
        rect.set(
                x + marginRect!!.left,
                y + marginRect!!.top,
                x + itemWidth + marginRect!!.left,
                y + itemHeight + marginRect!!.top)
        allItemFrames.put(position, rect)
    }

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun scrollHorizontallyBy(
            dx: Int, recycler: RecyclerView.Recycler, state: RecyclerView.State?): Int {
        var travel: Int
        dir = sign(dx.toFloat()).toInt()
        val mState = 0
        if (mRecyclerView != null)
            if (mState == 0) {
                if (!atEdge) {
                    atEdge = true
                    edgeDir = dir
                    mRecyclerView.postDelayed(
                            { startedAnimation = false },
                            500)
                    mRecyclerView.postDelayed(
                            {
                                if (!startedAnimation) {
                                    startedAnimation = true
                                    atEdge = false
                                }
                            },
                            700)
                }

                if (!startedAnimation) {
                    startedAnimation = true
                    if (edgeDir == dir) {
                        travel = edgeDir * mRecyclerView.width
                        if (offsetX + edgeDir * mRecyclerView.width < 0) {
                            travel = -offsetX
                        } else if (offsetX + edgeDir * mRecyclerView.width > totalWidth - horizontalSpace) { // 如果滑动到最底部
                            travel = totalWidth - horizontalSpace - offsetX
                        }
                        if (animator == null) {
                            animator = ValueAnimator.ofInt(0, travel)
                            animator!!.duration = 500
                            animator!!.addListener(animatorListener)
                        } else {
                            animator!!.setIntValues(0, travel)
                        }
                        animator!!.removeUpdateListener(updateListener)
                        updateListener = PageUpdateListener(recycler)
                        animator!!.addUpdateListener(updateListener)
                        animator!!.start()
                    } else {
                        atEdge = false
                    }
                }
                return 0
            }

        // 列表向下滚动dy为正，列表向上滚动dy为负，这点与Android坐标系保持一致。
        // 实际要滑动的距离
        travel = dx
        // 如果滑动到最顶部
        if (offsetX + dx < 0) {
            travel = -offsetX
        } else if (offsetX + dx > totalWidth - horizontalSpace) { // 如果滑动到最底部
            travel = totalWidth - horizontalSpace - offsetX
        }
        // 将竖直方向的偏移量+travel
        offsetX += travel
        // 调用该方法通知view在y方向上移动指定距离
        offsetChildrenHorizontal(-travel)
        recycleAndFillItems(recycler)
        return travel
    }

    override fun canScrollHorizontally(): Boolean {
        return true
    }

    override fun canScrollVertically(): Boolean {
        return false
    }

    inner class PageAnimatorListener : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            atEdge = false
        }
    }

    inner class PageUpdateListener(internal var recycler: RecyclerView.Recycler) : ValueAnimator.AnimatorUpdateListener {
        private var lastValue: Int = 0

        override fun onAnimationUpdate(animation: ValueAnimator) {
            val value = animation.animatedValue as Int - lastValue
            lastValue = animation.animatedValue as Int
            // 将竖直方向的偏移量+travel
            offsetX += value
            // 调用该方法通知view在x方向上移动指定距离
            offsetChildrenHorizontal(-value)
            recycleAndFillItems(recycler)
        }
    }
}
