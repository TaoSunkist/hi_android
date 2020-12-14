package top.thsunkist.brainhealthy.ui.reusable.views.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import top.thsunkist.brainhealthy.R
import top.thsunkist.brainhealthy.ui.reusable.views.list.ItemListViewWrapper.Companion.VIEW_TYPE_HEAD
import top.thsunkist.brainhealthy.utilities.weak
import com.scwang.smartrefresh.header.MaterialHeader
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import es.dmoral.toasty.Toasty
import io.reactivex.disposables.CompositeDisposable
import top.thsunkist.brainhealthy.utilities.view.Dimens

private class LoadingViewHolder(v: View) : ViewHolder(v) {
    var progressBar: ProgressBar = v.findViewById(R.id.cell_loading_progress_bar)
    var statusBarText: TextView = v.findViewById(R.id.cell_loading_status_text)
    /*var retryButton: Button = v.findViewById(R.id.cell_loading_retry_button)*/

    fun setLoading() {
        progressBar.visibility = View.VISIBLE
        statusBarText.visibility = View.INVISIBLE
        /*retryButton.visibility = View.INVISIBLE
        retryButton.isEnabled = false*/
    }

    fun setMessage(message: String) {
        progressBar.visibility = View.INVISIBLE
        statusBarText.text = message
        statusBarText.visibility = View.VISIBLE
        /*retryButton.visibility = View.INVISIBLE
        retryButton.isEnabled = false*/
    }

    fun setRetry() {
        progressBar.visibility = View.INVISIBLE
        statusBarText.visibility = View.INVISIBLE
        /*retryButton.visibility = View.VISIBLE
        retryButton.isEnabled = true*/
    }
}

open class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

    open val clickableView: View?
        get() = itemView
}

private class EmptyViewHolder(v: View) : ViewHolder(v)

class GenericListAdapter<T> : RecyclerView.Adapter<ViewHolder>(), View.OnClickListener {

    companion object {
        const val VIEW_TYPE_ITEM = 0
        const val VIEW_TYPE_LOADING = -1
    }

    interface ItemListCallback<T> {
        fun onListOnItemSelected(item: T, position: Int)
        fun onListShouldLoadNextPage(page: Int)
    }

    /* needed this to correct loading/error cell view style */
    var recyclerViewLayoutManager: RecyclerView.LayoutManager? = null

    var items = arrayListOf<T>()
    var delegate: ItemListViewWrapper<T>? by weak()
    private var nextPage: Int = 1
    private var error: Throwable? = null

    override fun getItemViewType(position: Int): Int {
        if (position < this.items.size) {
            return delegate?.doGetViewType(item = items[position], position = position)
                ?: VIEW_TYPE_ITEM
        }
        return VIEW_TYPE_LOADING
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
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
                            .inflate(R.layout.cell_row_icon_title, parent, false)
                    )
                holder.clickableView?.setOnClickListener(this)
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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
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

            when {
                error != null -> {
                    holder.itemView.layoutParams = commonItemViewLayoutParams
                    holder.setRetry()
                }
                nextPage == -1 -> {
                    holder.itemView.layoutParams =
                        ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0)
                    /* No more items */
                    holder.setMessage("")
                }
                else -> {
                    holder.itemView.layoutParams = commonItemViewLayoutParams
                    if (nextPage > 1) {
                        /* if first page loaded, show load more */
                        holder.setLoading()
                    }
                    /*else if (nextPage == 1) {
                         if first page load, don't display load more ui.
                    }*/
                    delegate?.onListShouldLoadNextPage(nextPage)
                }
            }
        } else {
            holder.clickableView?.setTag(R.id.id_item_position, position)
            delegate?.doBindItemViewHolder(
                holder = holder,
                item = items[position],
                position = position
            )
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

abstract class ItemListViewWrapper<T>(private val view: View) : GenericListAdapter.ItemListCallback<T> {

    companion object {

        const val PAGE_NO_MORE = -1

        const val VIEW_TYPE_HEAD = 1
    }

    open var pageSize = 20

    interface ItemListViewWrapperDelegate<T> {

        /**
         * @param item actual object
         * @param position position in adapter
         * @param clickTag null if whole cell is clicked
         */
        fun listViewControllerOnItemSelected(item: T, position: Int, clickTag: String?)
    }

    private val onRefreshListener = OnRefreshListener {
        onListShouldLoadNextPage(1)
    }

    var swipeToRefreshView: SmartRefreshLayout =
        view.findViewById(R.id.common_id_swipe_refresh_layout)
        private set

    private var emptyTipsTextView: AppCompatTextView =
        (view.findViewById(R.id.empty_tip_text_view) as (AppCompatTextView)).apply {
            compoundDrawablePadding = Dimens.marginXLarge
            alpha = 0f
        }

    private var contentContainer: FrameLayout =
        view.findViewById(R.id.common_id_content_container)

    var recyclerView: RecyclerView = contentContainer.findViewById(R.id.common_id_recycler_view)
        private set

    private var adapter: GenericListAdapter<T>? by weak()

    protected var compositeDisposable = CompositeDisposable()

    var delegate: ItemListViewWrapperDelegate<T>? by weak()

    val items: List<T>?
        get() = this.adapter?.items

    init {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            layout.setDisableContentWhenRefresh(true)
            layout.setHeaderInsetStart(0f)
            layout.setHeaderTriggerRate(0.8f)
            MaterialHeader(context).apply {
                setColorSchemeResources(R.color.colorPrimary)
            }
        }
        swipeToRefreshView.setOnRefreshListener(onRefreshListener)
        val layoutManager = this.getLayoutManager(recyclerView.context)
        recyclerView.layoutManager = layoutManager

        val adapter = GenericListAdapter<T>().apply {
            delegate = this@ItemListViewWrapper
            recyclerViewLayoutManager = layoutManager
        }
        this.adapter = adapter
        recyclerView.adapter = adapter
        swipeToRefreshView.autoRefreshAnimationOnly()
    }

    open fun onDestroy() {
        compositeDisposable.clear()
    }

    override fun onListOnItemSelected(item: T, position: Int) {
        this.delegate?.listViewControllerOnItemSelected(item, position, null)
    }

    override fun onListShouldLoadNextPage(page: Int) {
        doLoadNextPage(page)
    }

    /**
     * Replace the data source with given items list.
     */
    fun setItems(items: List<T>, nextPage: Int) {
        swipeToRefreshView.finishRefresh()
        this.adapter?.setItems(items, nextPage)
        /* TODO if status are loading, needed extra the dispose */
        emptyTipsTextView.alpha = if (this.adapter?.items?.isNotEmpty() == true) 0f else 1f
    }

    fun clearItems() {
        this.adapter?.clearAll()
        emptyTipsTextView.alpha = if (items?.isNotEmpty() == true) 0f else 1f
    }

    /**
     * Append given items list at the end.
     */
    fun audoFillInItems(items: List<T>, page: Int, nextPage: Int) {
        swipeToRefreshView.finishRefresh()
        if (page == 1) {
            this.adapter?.setItems(items, nextPage)
        } else {
            this.adapter?.addItems(items, page, nextPage)
        }
        emptyTipsTextView.alpha = if (this.items?.isNotEmpty() == true) 0f else 1f
    }

    @SuppressWarnings("unused")
    fun insertItems(items: List<T>, index: Int = 0) {
        swipeToRefreshView.finishRefresh()
        this.adapter?.insertItems(items = items, index = index)
    }

    open fun removeAt(position: Int) {
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
        Toasty.normal(
            recyclerView.context,
            error.localizedMessage ?:""
        ).show()
    }

    /* Child must override to customize */

    abstract
    fun doCreateItemViewHolder(parent: ViewGroup, viewType: Int): ViewHolder

    abstract
    fun doBindItemViewHolder(holder: ViewHolder, item: T, position: Int)

    abstract
    fun doLoadNextPage(page: Int)

    open fun doGetViewType(item: T, position: Int): Int {
        return GenericListAdapter.VIEW_TYPE_ITEM
    }

    open fun getLayoutManager(context: Context): RecyclerView.LayoutManager {
        return LinearLayoutManager(view.context)
    }
}
