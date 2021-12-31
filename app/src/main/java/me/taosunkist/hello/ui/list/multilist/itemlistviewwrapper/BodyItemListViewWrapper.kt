package me.taosunkist.hello.ui.list.multilist.itemlistviewwrapper

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import io.reactivex.rxkotlin.addTo
import me.taosunkist.hello.R
import me.taosunkist.hello.data.net.AppService
import me.taosunkist.hello.databinding.CellCommonBinding
import me.taosunkist.hello.ui.list.multilist.CellUIModel
import me.taosunkist.hello.ui.reusable.itemlistviewwrapper.GenericListAdapter
import me.taosunkist.hello.ui.reusable.itemlistviewwrapper.ItemListViewWrapper
import me.taosunkist.hello.ui.reusable.itemlistviewwrapper.ViewHolder
import top.thsunkist.appkit.utility.printf
import top.thsunkist.tatame.utilities.bind
import top.thsunkist.tatame.utilities.observeOnMainThread

class BodyItemListViewWrapper(view: View) : ItemListViewWrapper<CellUIModel>(view = view) {

    override var pageSize: Int = 20

    private val secondItemAsyncListDiffer: SecondItemAsyncListDiffer = SecondItemAsyncListDiffer(adapter!!)

    override fun doCreateItemViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = SecondViewHolder(
        binding = CellCommonBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun doBindItemViewHolder(holder: ViewHolder, item: CellUIModel, position: Int) {
        holder.itemView.setTag(R.id.id_item_position, position)
        val cellUIModel = secondItemAsyncListDiffer.currentList.getOrNull(position)
        (holder as SecondViewHolder).bind(cellUIModel = cellUIModel)
    }

    override fun doLoadNextPage(page: Int) {
        AppService.shared.fetchMultiListDataList(
            pageSize = pageSize,
            pageIndex = page,
        ).map {
            it.list.map { CellUIModel.init(conversationItem = it) }
        }.observeOnMainThread(onSuccess = {
            secondItemAsyncListDiffer.submitList(it)
        }, onError = {
        }, onTerminate = {
            swipeToRefreshView.finishRefresh()
        }).addTo(compositeDisposable = compositeDisposable)
    }

    override fun getItemCount(): Int {
        return secondItemAsyncListDiffer.currentList.size
    }

    inner class SecondViewHolder(val binding: CellCommonBinding) : ViewHolder(binding.root) {

        fun bind(cellUIModel: CellUIModel?) {
            cellUIModel?.let {
                binding.iconCircleImageView.bind(cellUIModel.imageUIModel)
                binding.titleTextView.text = cellUIModel.nickname
                printf("SecondViewHolder", cellUIModel.nickname + ", bind")
            }
        }
    }
}

class SecondItemAsyncListDiffer(genericListAdapter: GenericListAdapter<CellUIModel>) :

    AsyncListDiffer<CellUIModel>(genericListAdapter, object : DiffUtil.ItemCallback<CellUIModel>() {

        override fun areItemsTheSame(oldItem: CellUIModel, newItem: CellUIModel): Boolean {
            return (oldItem.nickname + oldItem.imageUIModel.imageUrl) == (newItem.nickname + newItem.imageUIModel.imageUrl)
        }

        override fun areContentsTheSame(oldItem: CellUIModel, newItem: CellUIModel): Boolean {
            return (oldItem) == (newItem)
        }
    }) {

}

