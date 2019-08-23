package me.taosunkist.hello.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView

import java.util.ArrayList

import me.taosunkist.hello.R

/** Created by zhuguohui on 2016/11/8.  */
class MyAdapter : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_item, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tv_title.text = data[position]
        holder.itemView.setOnClickListener {
            data.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, data.size)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_title: TextView

        init {
            tv_title = itemView.findViewById<View>(R.id.tv_title) as TextView
        }
    }

    companion object {

        private val data = ArrayList<String>()

        init {
            for (i in 1..15) {
                data.add(i.toString() + "")
            }
        }
    }
}
