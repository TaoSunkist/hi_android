package com.example.tatame_frontline.ui.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tatame_frontline.R
import com.example.tatame_frontline.ui.home.viewholders.IdolsViewPagerViewHolder
import com.example.tatame_frontline.ui.home.viewholders.ProfileViewPagerViewHolder
import com.flyco.tablayout.listener.CustomTabEntity

data class IdolsCommonTabEntity(var title: String,
                                var selectedIcon: Int,
                                var unSelectedIcon: Int) : CustomTabEntity {

    override fun getTabTitle(): String {
        return title
    }

    override fun getTabSelectedIcon(): Int {
        return selectedIcon
    }

    override fun getTabUnselectedIcon(): Int {
        return unSelectedIcon
    }
}

class HomeViewPagerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            IdolsViewPagerViewHolder.ITEM_TYPE -> IdolsViewPagerViewHolder(itemView = LayoutInflater.from(parent.context).inflate(R.layout.pager_fragment_home_idols, parent, false))
            ProfileViewPagerViewHolder.ITEM_TYPE -> ProfileViewPagerViewHolder(itemView = LayoutInflater.from(parent.context).inflate(R.layout.pager_fragment_home_profile, parent, false))
            else -> TODO()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) IdolsViewPagerViewHolder.ITEM_TYPE else ProfileViewPagerViewHolder.ITEM_TYPE
    }

    override fun getItemCount() = run { 2 }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is IdolsViewPagerViewHolder) {
            holder.bind()
        } else if (holder is ProfileViewPagerViewHolder) {
            holder.bind()
        }
    }
}