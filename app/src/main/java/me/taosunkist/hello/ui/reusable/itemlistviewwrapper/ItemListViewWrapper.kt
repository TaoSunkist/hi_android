package me.taosunkist.hello.ui.reusable.itemlistviewwrapper

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.scwang.smartrefresh.header.MaterialHeader
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import io.reactivex.disposables.CompositeDisposable
import me.taosunkist.hello.R
import me.taosunkist.hello.ui.reusable.itemlistviewwrapper.ItemListViewWrapper.Companion.VIEW_TYPE_HEAD
import me.taosunkist.hello.utility.Dimens
import top.thsunkist.tatame.utilities.weak

/** 加载的loadingView Holoder */
private class LoadingViewHolder(v: View) : ViewHolder(v) {
    var progressBar: ProgressBar = v.findViewById(R.id.cell_loading_progress_bar)
    var statusBarText: TextView = v.findViewById(R.id.cell_loading_status_text)
    /*var retryButton: Button = v.findViewById(R.id.cell_loading_retry_button)*/

    fun setLoading() {
        progressBar.visibility = View.VISIBLE
        statusBarText.visibility = View.GONE
        /*retryButton.visibility = View.INVISIBLE
        retryButton.isEnabled = false*/
    }

    fun setMessage(message: String) {
        progressBar.visibility = View.GONE
        statusBarText.text = message
        statusBarText.visibility = View.VISIBLE
        /*retryButton.visibility = View.INVISIBLE
        retryButton.isEnabled = false*/
    }

    fun setRetry() {
        progressBar.visibility = View.GONE
        statusBarText.visibility = View.GONE
        /*retryButton.visibility = View.VISIBLE
        retryButton.isEnabled = true*/
    }
}

/**
 * viewholoder的底部类
 */
open class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

    open val clickableView: View?
        get() = itemView
}

/**
 * 空白的viewholder
 */
private class EmptyViewHolder(v: View) : ViewHolder(v)

/**
 * item的回调
 */
interface ItemListCallback<T> {
    fun onListOnItemSelected(item: T, position: Int)
    fun onListShouldLoadNextPage(page: Int)
    fun onViewAttachedToWindow(holder: ViewHolder) {}
    fun onLongListOnItemSelected(item: T, position: Int) {}
}

/**
 * 通用的List  Adapter
 */
open class GenericListAdapter<T> : RecyclerView.Adapter<ViewHolder>(), View.OnClickListener, View.OnLongClickListener {
    //区别item 或者 加载的view
    companion object {
        const val VIEW_TYPE_ITEM = 0
        const val VIEW_TYPE_LOADING = -1
        private const val TAG = "ItemListViewWrapper"
    }

    /* 需要这个来纠正loadingerror单元格视图样式 needed this to correct loading/error cell view style */
    var recyclerViewLayoutManager: RecyclerView.LayoutManager? = null

    var items = arrayListOf<T>()    //数据流
    var delegate: ItemListViewWrapper<T>? by weak()
    private var nextPage: Int = 1 //新的一页
    private var error: Throwable? = null //错误

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        delegate?.onViewAttachedToWindow(holder)
        Log.d(TAG, "onViewAttachedToWindow:--------- ")
    }

    /**
     * 返回item的类型
     */
    override fun getItemViewType(position: Int): Int {
        if (position < this.items.size) {
            return delegate?.doGetViewType(item = items[position], position = position)
                ?: VIEW_TYPE_ITEM
        }
        return VIEW_TYPE_LOADING
    }


    /**
     * 创建viewholder的控件布局
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d(TAG, "onCreateViewHolder:--------- ")
        return when (viewType) {
            VIEW_TYPE_LOADING -> {
                val holder = LoadingViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.cell_loading, parent, false)
                )
//                holder.retryButton.setOnClickListener {
//                    this.error = null
//                    notifyItemChanged(this.items.size)
//                }
                holder
            }
            else -> {
                val holder = delegate?.doCreateItemViewHolder(parent, viewType)
                    ?: EmptyViewHolder(
                        LayoutInflater.from(parent.context)
                            .inflate(R.layout.cell_common, parent, false)
                    )
                holder.clickableView?.setOnClickListener(this)
                holder.clickableView?.setOnLongClickListener(this)
                holder
            }
        }
    }

    override fun onClick(v: View?) {
        val position: Int = (v?.getTag(R.id.id_item_position) as? Int) ?: -1
        if (position >= 0) {
            this.delegate?.onListOnItemSelected(items[position], position)
        }
    }

    override fun onLongClick(v: View?): Boolean {
        val position: Int = (v?.getTag(R.id.id_item_position) as? Int) ?: -1
        if (position >= 0) {
            this.delegate?.onLongListOnItemSelected(items[position], position)
            return true
        }
        return false
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        if (recyclerView.layoutManager is GridLayoutManager) {
            (recyclerView.layoutManager as GridLayoutManager).spanSizeLookup =
                    /* add subtitle column for recyclerview and loading column */
                object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int =
                        when {
                            getItemViewType(position) == VIEW_TYPE_LOADING -> (recyclerView.layoutManager as GridLayoutManager).spanCount
                            getItemViewType(position) == VIEW_TYPE_HEAD -> (recyclerView.layoutManager as GridLayoutManager).spanCount
                            else -> 1
                        }

                }
        }
    }

    override fun getItemCount(): Int {
        return items.size + 1
    }

    //view的绑定
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder:--------- ")
        //如果holder属于loadingViewHolder 则对其不同的状态进行处理 这个loadingViewHolder 是foot的 head是 SmartRecyclerView自己自带的
        if (holder is LoadingViewHolder) {

            /* badly code... */
            val commonItemViewLayoutParams = if (recyclerViewLayoutManager is LinearLayoutManager) {
                if ((recyclerViewLayoutManager as LinearLayoutManager).orientation == LinearLayoutManager.HORIZONTAL) {
                    ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                } else {
                    ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                }
            } else {
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }

            when { //根据不同的状态 对布局进行不同的展示
                error != null -> {
                    holder.itemView.layoutParams = commonItemViewLayoutParams
                    holder.setRetry()
                }
                nextPage == -1 -> {
                    holder.itemView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 0)
                    /* No more items */
                    holder.setMessage("")
                }
                else -> {
                    holder.itemView.layoutParams = commonItemViewLayoutParams
                    if (nextPage > 1) {
                        /* if first page loaded, show load more */
                        holder.setLoading()
                        delegate?.onListShouldLoadNextPage(nextPage)
                    }
                    /*else if (nextPage == 1) {
                         if first page load, don't display load more ui.
                    }*/
                }
            }
        } else {//如果holder属于正常的item则正常展示对itemviewHolder设置tag
            if (position < items.size) {
                holder.clickableView?.setTag(R.id.id_item_position, position)
                delegate?.doBindItemViewHolder(
                    holder = holder,
                    item = items[position],
                    position = position
                )
            }
        }
    }

    /**
     * Replace the data source with given items list.
     */
    fun setItems(friends: List<T>, nextPage: Int) {
        this.error = null
        this.nextPage = nextPage
        this.items.clear()
        this.items.addAll(friends)
        notifyDataSetChanged()
    }

    /**
     * Append given items list at the end.
     */
    fun addItems(items: List<T>, page: Int, nextPage: Int) {
        if (this.nextPage == page) {
            this.nextPage = nextPage
            val startPosition = this.items.size
            this.items.addAll(items)
            notifyItemRangeChanged(startPosition, items.size)
            notifyItemChanged(this.items.size)
        }
    }

    fun insertItems(items: List<T>, index: Int = 0) {
        this.items.addAll(index, items)
        notifyItemRangeInserted(index, items.size)
    }

    /**
     * Replace and reload given item/position.
     */
    fun reloadItem(item: T, index: Int) {
        if (items.indices.contains(index)) {
            items[index] = item
            notifyItemChanged(index, Unit)
        }
    }

    fun clearAll() {
        this.error = null
        this.nextPage = 1
        val itemsCount = this.items.size
        this.items.clear()
        this.notifyItemRangeRemoved(0, itemsCount)
        notifyItemChanged(this.items.size)
    }

    fun removeAt(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, items.count())
    }

    fun onError(error: Throwable) {
        this.error = error
        notifyItemChanged(this.items.size)
    }


}

interface ItemListViewWrapperDelegate<T> {

    /**
     * @param item actual object
     * @param position position in adapter
     * @param clickTag null if whole cell is clicked
     */
    fun listViewControllerOnItemSelected(item: T, position: Int, clickTag: String?)
}

/**
 * 自定义的 一个下拉刷新 上拉加载的控件 传入的View必须含有SmartRefreshLayout 控件等
 */
abstract class ItemListViewWrapper<T>(private val view: View) : ItemListCallback<T> {

    companion object {

        const val PAGE_NO_MORE = -1

        const val VIEW_TYPE_HEAD = 1
    }

    open var pageSize = 100

    //刷新控件的监听
    private val onRefreshListener = OnRefreshListener {
        onListShouldLoadNextPage(1)
    }

    //刷新控件
    var swipeToRefreshView: SmartRefreshLayout =
        view.findViewById(R.id.common_id_swipe_refresh_layout)
        private set

    //空白的提示页面
    private var emptyTipsTextView: AppCompatTextView =
        (view.findViewById(R.id.empty_tip_text_view) as (AppCompatTextView)).apply {
            compoundDrawablePadding = Dimens.marginXLarge
            alpha = 0f
        }

    //SmartRefreshLayout包含下的控件
    private var contentContainer: ViewGroup =
        view.findViewById(R.id.common_id_content_container)

    //recyclerView控件
    var recyclerView: RecyclerView = contentContainer.findViewById(R.id.common_id_recycler_view)
        private set

    //封装了的adapter
    private var adapter: GenericListAdapter<T>? by weak()

    protected var compositeDisposable = CompositeDisposable()

    var delegate: ItemListViewWrapperDelegate<T>? by weak()

    //adapter存储的数据
    val items: List<T>?
        get() = this.adapter?.items

    //初始化一下控件
    init {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            layout.setDisableContentWhenRefresh(true)
            layout.setHeaderInsetStart(0f)
            layout.setEnableNestedScroll(true)
            layout.setHeaderTriggerRate(1f)
            MaterialHeader(context).apply {
                setColorSchemeResources(R.color.colorPrimary)
            }
        }
        swipeToRefreshView.setOnRefreshListener(onRefreshListener)
        swipeToRefreshView.autoRefresh()
        val layoutManager = this.getLayoutManager(recyclerView.context)
        recyclerView.layoutManager = layoutManager

        val adapter = GenericListAdapter<T>().apply {
            delegate = this@ItemListViewWrapper
            recyclerViewLayoutManager = layoutManager
        }
        this.adapter = adapter
        recyclerView.adapter = adapter
    }

    open fun onDestroy() {
        compositeDisposable.clear()
    }

    override fun onListOnItemSelected(item: T, position: Int) {
        this.delegate?.listViewControllerOnItemSelected(item, position, null)
    }

    /**
     *     刷新控件的监听
     */
    override fun onListShouldLoadNextPage(page: Int) {
        doLoadNextPage(page)
    }

    /**
     * Replace the data source with given items list.
     */
    fun setItems(items: List<T>, nextPage: Int) {
        this.adapter?.setItems(items, nextPage)
        /* TODO if status are loading, needed extra the dispose */
        emptyTipsTextView.alpha = if (this.adapter?.items?.isNotEmpty() == true) 0f else 1f
        swipeToRefreshView.finishRefresh()
    }

    fun clearItems() {
        this.adapter?.clearAll()
        emptyTipsTextView.alpha = if (items?.isNotEmpty() == true) 0f else 1f
    }

    /**
     * Append given items list at the end.在末尾附加给定的项目列表
     */
    fun fillInItemsByPageIndex(items: List<T>, page: Int) {
        var nextPage = page + 1
        if (items.size < pageSize) {
            nextPage = PAGE_NO_MORE
        }
        if (page == 1) {
            this.adapter?.setItems(items, nextPage)
        } else {
            this.adapter?.addItems(items, page, nextPage)
        }
        swipeToRefreshView.finishRefresh()
        emptyTipsTextView.alpha = if (this.items?.isNotEmpty() == true) 0f else 1f
    }

    @SuppressWarnings("unused")
    fun insertItems(items: List<T>, index: Int = 0) {
        this.adapter?.insertItems(items = items, index = index)
        swipeToRefreshView.finishRefresh()
    }

    open fun removeAt(position: Int) {
        if (this.items?.size == 0) {
            return
        }
        this.adapter?.removeAt(position = position)
        emptyTipsTextView.alpha = if (items?.isNotEmpty() == true) 0f else 1f
    }

    /**
     * Replace and reload given item/position.
     */
    fun reloadItem(item: T, index: Int) {
        this.adapter?.reloadItem(item, index)
    }

    /**
     * Set error.
     */
    fun onError(error: Throwable) {
        error.printStackTrace()
        swipeToRefreshView.finishRefresh()
        this.adapter?.onError(error)
    }

    /* Child must override to customize */

    abstract
    fun doCreateItemViewHolder(parent: ViewGroup, viewType: Int): ViewHolder

    /**
     * 在Adapter中的onBindViewHolder的绑定
     * @param holder 当前viewholder的布局
     * @param item 当前viewholder的数据
     * @param position 当前item的位置
     */
    abstract
    fun doBindItemViewHolder(holder: ViewHolder, item: T, position: Int)

    /**
     * 获取新的页面
     */
    abstract
    fun doLoadNextPage(page: Int)

    open fun doGetViewType(item: T, position: Int): Int {
        return GenericListAdapter.VIEW_TYPE_ITEM
    }

    //设置LayoutManager的管理器
    open fun getLayoutManager(context: Context): RecyclerView.LayoutManager {
        return CustomLinearLayoutManager(view.context)
    }
}
