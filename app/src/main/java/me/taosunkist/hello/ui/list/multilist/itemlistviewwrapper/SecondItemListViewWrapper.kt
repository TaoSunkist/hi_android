package me.taosunkist.hello.ui.list.multilist.itemlistviewwrapper

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.rxkotlin.addTo
import me.taosunkist.hello.R
import me.taosunkist.hello.data.net.AppService
import me.taosunkist.hello.databinding.CellCommonBinding
import me.taosunkist.hello.ui.list.multilist.ConversationCellUIModel
import me.taosunkist.hello.ui.list.multilist.itemlistviewwrapper.viewholder.SecondViewHolder
import me.taosunkist.hello.ui.reusable.itemlistviewwrapper.ItemListViewWrapper
import me.taosunkist.hello.ui.reusable.itemlistviewwrapper.ViewHolder
import top.thsunkist.tatame.utilities.observeOnMainThread

/**
 * 首页im消息相关的ListVie封装类
 */
class SecondItemListViewWrapper(view: View) : ItemListViewWrapper<ConversationCellUIModel>(view = view) {

    val positionUIDMap = hashMapOf<String, Int>()

    override var pageSize: Int = 20

    //创建item holder
    override fun doCreateItemViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = SecondViewHolder(
        binding = CellCommonBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    //绑定 item holder
    override fun doBindItemViewHolder(holder: ViewHolder, item: ConversationCellUIModel, position: Int) {
        holder.itemView.setTag(R.id.id_item_position, position)
        (holder as SecondViewHolder).bind(item)
    }

    //获取新的数据
    override fun doLoadNextPage(pageIndex: Int) {
        AppService.shared.fetchMultiListDataList(
            pageSize = pageSize,
            pageIndex = pageIndex,
        ).map {

            if (pageIndex == 1) {
                positionUIDMap.clear()
                (items as ArrayList).clear()
            }
            it.list.mapIndexed { _, conversationItem ->
                ConversationCellUIModel.init(conversationItem = conversationItem)
            }
        }.observeOnMainThread(onSuccess = { conversationCellUIModels ->

            var nextPage = pageIndex + 1
            val messageCount = conversationCellUIModels.size
            if (messageCount < pageSize) {
                nextPage = PAGE_NO_MORE
            }


            items?.toMutableList()?.let {
                clearItems()
                it.addAll(it)
                setItems(items = it, nextPage = nextPage)
            }
        }, onError = {
        }, onTerminate = {
            swipeToRefreshView.finishRefresh()
        }).addTo(compositeDisposable = compositeDisposable)
    }

    fun reset() {
        clearItems()
        compositeDisposable.clear()
    }

    //点击事件
    override fun onListOnItemSelected(item: ConversationCellUIModel, position: Int) {
    }

    //长按点击事件
    override fun onLongListOnItemSelected(item: ConversationCellUIModel, position: Int) {
    }
}

