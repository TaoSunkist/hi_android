package me.taosunkist.hello.ui.list.multilist.itemlistviewwrapper

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.rxkotlin.addTo
import me.taosunkist.hello.data.net.AppService
import me.taosunkist.hello.databinding.CellCommonSquareBinding
import me.taosunkist.hello.ui.list.multilist.CelllUIModel
import me.taosunkist.hello.ui.reusable.itemlistviewwrapper.CustomLinearLayoutManager
import me.taosunkist.hello.ui.reusable.itemlistviewwrapper.ItemListViewWrapper
import me.taosunkist.hello.ui.reusable.itemlistviewwrapper.ViewHolder
import top.thsunkist.tatame.utilities.bind
import top.thsunkist.tatame.utilities.observeOnMainThread

class FirstListViewWrapper(val view: View) : ItemListViewWrapper<CelllUIModel>(view) {

    override var pageSize: Int = 50

    override fun doCreateItemViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        FirstViewHolder(CellCommonSquareBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun doBindItemViewHolder(holder: ViewHolder, item: CelllUIModel, position: Int) =
        (holder as FirstViewHolder).bind(item)

    override fun doLoadNextPage(page: Int) {
        AppService.shared.fetchMultiListDataList(pageIndex = page, pageSize = pageSize).map {
            it.list.map { CelllUIModel.init(it) }
        }.observeOnMainThread(onSuccess = {

            if (page == 1 && it.isEmpty()) {
                view.visibility = View.GONE
            } else {
                view.visibility = View.VISIBLE
            }
            fillInItemsByPageIndex(it, page)
        }, onError = {
        }, onTerminate = {
        }).addTo(compositeDisposable = compositeDisposable)
    }

    override fun getLayoutManager(context: Context): RecyclerView.LayoutManager =
        CustomLinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
}


class FirstViewHolder(val binding: CellCommonSquareBinding) : ViewHolder(binding.root) {

    fun bind(item: CelllUIModel) {

        binding.corneredImageView.bind(item.imageUIModel)
        binding.titleTextView.text = item.nickname
    }
}