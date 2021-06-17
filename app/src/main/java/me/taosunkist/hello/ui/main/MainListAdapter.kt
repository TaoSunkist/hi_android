package me.taosunkist.hello.ui.main

import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mooveit.library.Fakeit

class MainListAdapter : ListAdapter<String, MainViewHolder>(object : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return false
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return false
    }
}) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(AppCompatTextView(parent.context))
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind()
    }
}

class MainViewHolder(private val contentTextView: AppCompatTextView) : RecyclerView.ViewHolder(contentTextView) {

    fun bind() {
        contentTextView.text = Fakeit.pokemon().name()
    }
}