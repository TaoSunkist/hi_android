package me.taosunkist.hello.ui.list.multilist.itemlistviewwrapper

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.rxkotlin.addTo
import me.taosunkist.hello.R
import me.taosunkist.hello.data.net.AppService
import me.taosunkist.hello.databinding.CellCommonBinding
import me.taosunkist.hello.ui.list.multilist.CelllUIModel
import me.taosunkist.hello.ui.reusable.itemlistviewwrapper.ItemListViewWrapper
import me.taosunkist.hello.ui.reusable.itemlistviewwrapper.ViewHolder
import top.thsunkist.tatame.utilities.bind
import top.thsunkist.tatame.utilities.observeOnMainThread

class SecondItemListViewWrapper(view: View) : ItemListViewWrapper<CelllUIModel>(view = view) {

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
    override fun doBindItemViewHolder(holder: ViewHolder, item: CelllUIModel, position: Int) {
        holder.itemView.setTag(R.id.id_item_position, position)
        (holder as SecondViewHolder).bind(item)
    }

    //获取新的数据
    override fun doLoadNextPage(pageIndex: Int) {
        AppService.shared.fetchMultiListDataList(
            pageSize = pageSize,
            pageIndex = pageIndex,
        ).map {
            it.list.map { CelllUIModel.init(conversationItem = it) }
        }.observeOnMainThread(onSuccess = {
            addItems(it, pageIndex)
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
    override fun onListOnItemSelected(item: CelllUIModel, position: Int) {
    }

    //长按点击事件
    override fun onLongListOnItemSelected(item: CelllUIModel, position: Int) {
    }
}


class SecondViewHolder(val binding: CellCommonBinding) : ViewHolder(binding.root) {

    companion object {
    }

    fun bind(
        cellUIModel: CelllUIModel,
    ) {
        binding.iconCircleImageView.bind(cellUIModel.imageUIModel)
        binding.titleTextView.text = cellUIModel.nickname
    }
}