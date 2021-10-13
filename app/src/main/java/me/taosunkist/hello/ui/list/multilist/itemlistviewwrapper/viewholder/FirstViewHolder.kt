package me.taosunkist.hello.ui.list.multilist.itemlistviewwrapper.viewholder

import me.taosunkist.hello.databinding.CellCommonSquareBinding
import me.taosunkist.hello.ui.list.multilist.ConversationCellUIModel
import me.taosunkist.hello.ui.reusable.itemlistviewwrapper.ViewHolder
import top.thsunkist.tatame.utilities.bind

class FirstViewHolder(val binding: CellCommonSquareBinding) : ViewHolder(binding.root) {

    fun bind(item: ConversationCellUIModel) {

        binding.corneredImageView.bind(item.imageUIModel)
        binding.titleTextView.text = item.nickname
    }
}