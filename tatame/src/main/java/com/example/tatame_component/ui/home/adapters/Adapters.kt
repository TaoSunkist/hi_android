package com.example.tatame_component.ui.home.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tatame_component.ui.home.viewholders.IdolsListViewHolder

class IdolsListViewPagerAdapter : RecyclerView.Adapter<IdolsListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IdolsListViewHolder {
        return IdolsListViewHolder(RecyclerView(parent.context))
    }

    override fun getItemCount() = run { 2 }

    override fun onBindViewHolder(holder: IdolsListViewHolder, position: Int) {
        holder.bind(position)
    }
}

//class FavoriteIdolsAdapter:RecyclerView.Adapter<>
//
//class AllIdolsAdapter()
