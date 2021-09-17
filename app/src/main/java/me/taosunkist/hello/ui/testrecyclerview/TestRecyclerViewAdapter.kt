package me.taosunkist.hello.ui.testrecyclerview

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import me.taosunkist.hello.databinding.FragmentItemTestRecyclerViewListBinding

import me.taosunkist.hello.ui.testrecyclerview.placeholder.PlaceholderContent.PlaceholderItemUIModel
import me.taosunkist.hello.utility.printf

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItemUIModel].
 * TODO: Replace the implementation with code for your data type.
 */
class TestRecyclerViewAdapter(
    private val itemUIModels: List<PlaceholderItemUIModel>,
) : RecyclerView.Adapter<TestRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(FragmentItemTestRecyclerViewListBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemUIModel = itemUIModels[position]
        holder.idView.text = itemUIModel.title
        holder.contentView.text = itemUIModel.author

        printf("taohui ${holder.itemId} $position")
    }

    override fun getItemCount(): Int = itemUIModels.size

    inner class ViewHolder(binding: FragmentItemTestRecyclerViewListBinding) : RecyclerView.ViewHolder(binding.root) {

        val idView: TextView = binding.itemNumber

        val contentView: TextView = binding.content

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }
}