package me.taosunkist.hello.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.taosunkist.hello.databinding.FragmentMainBinding

class MainFragment : NavHostFragment() {

    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = FragmentMainBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = object : ListAdapter<String, MainViewHolder>(object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return false
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return false
            }
        }) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
                return MainViewHolder(AppCompatTextView(requireContext()))
            }

            override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
                holder.bind()
            }
        }
    }
}

class MainViewHolder(itemTextView: AppCompatTextView) : RecyclerView.ViewHolder(itemTextView) {

    fun bind() {

    }
}