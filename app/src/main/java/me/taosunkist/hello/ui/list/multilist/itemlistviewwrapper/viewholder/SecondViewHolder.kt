package me.taosunkist.hello.ui.list.multilist.itemlistviewwrapper.viewholder

import me.taosunkist.hello.databinding.CellCommonBinding
import me.taosunkist.hello.ui.list.multilist.ConversationCellUIModel
import me.taosunkist.hello.ui.reusable.itemlistviewwrapper.ViewHolder
import top.thsunkist.tatame.utilities.bind

class SecondViewHolder(val binding: CellCommonBinding) : ViewHolder(binding.root) {

    companion object {
    }

    fun bind(
        cellUIModel: ConversationCellUIModel,
    ) {
        binding.iconCircleImageView.bind(cellUIModel.imageUIModel)
        binding.titleTextView.text = cellUIModel.nickname
    }
}